package cz.tefek.pluto.engine.audio;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;

import cz.tefek.pluto.engine.buffer.BufferHelper;
import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public class AudioLoader
{
    /**
     * Loads an audio track denoted by this {@link ResourceAddress} into memory
     * for from-memory Vorbis decoding and streaming. Good for frequently used
     * medium sized audio files, however it is discouraged to use such a track
     * in multiple audio sources at once due to the cost of seeking.
     */
    public static ISeekableAudioTrack loadMemoryDecoded(ResourceAddress address)
    {

        Logger.logf(SmartSeverity.AUDIO_PLUS, "Loading audio file: %s\n", address.toString());

        try
        {
            return new MemoryDecodedVorbisTrack(address);
        }
        catch (IOException e)
        {
            Logger.logf(SmartSeverity.AUDIO_ERROR, "Failed to load an audio file: '%s'\n", address.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads an audio track denoted by this {@link ResourceAddress} into memory
     * for from-memory PCM streaming. Good for frequently used small audio
     * files.
     */
    public static ISeekableAudioTrack loadMemoryPCM(ResourceAddress address)
    {
        Logger.logf(SmartSeverity.AUDIO_PLUS, "Loading audio file: %s\n", address.toString());

        try
        {
            return new MemoryPCMTrack(address);
        }
        catch (IOException e)
        {
            Logger.logf(SmartSeverity.AUDIO_ERROR, "Failed to load an audio file: '%s'\n", address.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static ByteBuffer loadIntoMemory(ResourceAddress addr) throws IOException
    {
        var path = addr.toNIOPath();
        var size = Files.size(path);

        if (size > Integer.MAX_VALUE)
        {
            throw new IOException("File '" + addr.toString() + "' is too big to be loaded!");
        }

        var readData = MemoryUtil.memAlloc((int) size);

        return BufferHelper.readToByteBuffer(path, readData);
    }

    private abstract static class StreamableTrack implements IAudioStream
    {
        protected int channels;
        protected int sampleRate;

        @Override
        public int getChannels()
        {
            return this.channels;
        }

        @Override
        public int getSampleRate()
        {
            return this.sampleRate;
        }
    }

    private abstract static class SeekableTrack extends StreamableTrack implements ISeekableAudioTrack
    {
        protected int samplesLength;

        @Override
        public int getLengthInSamples()
        {
            return this.samplesLength;
        }
    }

    public static class MemoryPCMTrack extends SeekableTrack
    {
        private ShortBuffer pcmAudio = null;

        private int sampleOffset = 0;

        private MemoryPCMTrack(ResourceAddress address) throws IOException
        {
            long handle = MemoryUtil.NULL;
            ByteBuffer audioBytes = null;

            try (MemoryStack stack = MemoryStack.stackPush())
            {
                audioBytes = loadIntoMemory(address);

                IntBuffer error = stack.mallocInt(1);
                handle = STBVorbis.stb_vorbis_open_memory(audioBytes, error, null);

                if (handle == MemoryUtil.NULL)
                {
                    this.close();
                    throw new IOException(String.format("Failed to load '%s', error code %d.\n", address.toString(), error.get(0)));
                }

                STBVorbisInfo info = STBVorbisInfo.mallocStack(stack);
                STBVorbis.stb_vorbis_get_info(handle, info);

                this.channels = info.channels();
                this.sampleRate = info.sample_rate();

                // Downmix to mono, SOUNDS HORRIBLE
                //
                // this.channels = 1;
                // this.samplesLength = STBVorbis.stb_vorbis_stream_length_in_samples(handle) * this.channels;
                // this.pcmAudio = MemoryUtil.memAllocShort(this.samplesLength);
                // var ptr = stack.pointers(this.pcmAudio);
                // STBVorbis.stb_vorbis_get_samples_short(handle, ptr, this.samplesLength);

                this.samplesLength = STBVorbis.stb_vorbis_stream_length_in_samples(handle) * this.channels;
                this.pcmAudio = MemoryUtil.memAllocShort(this.samplesLength);
                STBVorbis.stb_vorbis_get_samples_short_interleaved(handle, this.channels, this.pcmAudio);
            }
            catch (IOException e)
            {
                this.close();
                throw e;
            }
            finally
            {
                MemoryUtil.memFree(audioBytes);

                if (handle != MemoryUtil.NULL)
                {
                    STBVorbis.stb_vorbis_close(handle);
                }
            }
        }

        @Override
        public void seek(int sampleIndex)
        {
            this.sampleOffset = sampleIndex * this.getChannels();
        }

        @Override
        public synchronized int getSamples(ShortBuffer pcm)
        {
            this.pcmAudio.limit(Math.min(this.sampleOffset + pcm.remaining(), this.pcmAudio.capacity()));
            int read = this.pcmAudio.remaining();
            pcm.put(this.pcmAudio);
            this.sampleOffset += read;
            pcm.clear();

            return read / this.getChannels();
        }

        @Override
        public int getSampleOffset()
        {
            return this.sampleOffset / this.getChannels();
        }

        @Override
        public void close()
        {
            MemoryUtil.memFree(this.pcmAudio);
        }
    }

    public static class MemoryDecodedVorbisTrack extends SeekableTrack
    {
        protected long handle;

        private final ByteBuffer encodedAudio;

        private MemoryDecodedVorbisTrack(ResourceAddress address) throws IOException
        {
            try
            {
                this.encodedAudio = loadIntoMemory(address);
            }
            catch (IOException e)
            {
                this.close();
                throw e;
            }

            try (MemoryStack stack = MemoryStack.stackPush())
            {
                IntBuffer error = stack.mallocInt(1);
                this.handle = STBVorbis.stb_vorbis_open_memory(this.encodedAudio, error, null);

                if (this.handle == MemoryUtil.NULL)
                {
                    this.close();
                    throw new IOException(String.format("Failed to load '%s', error code %d.\n", address.toString(), error.get(0)));
                }

                STBVorbisInfo info = STBVorbisInfo.mallocStack(stack);
                STBVorbis.stb_vorbis_get_info(this.handle, info);

                this.channels = info.channels();
                this.sampleRate = info.sample_rate();

            }

            this.samplesLength = STBVorbis.stb_vorbis_stream_length_in_samples(this.handle);

            Logger.logf(SmartSeverity.AUDIO, "\tSample rate:\t%d\n\t\tChannels:\t%d\n\t\tSamples:\t%d\n", this.sampleRate, this.channels, this.samplesLength);
        }

        @Override
        public synchronized int getSamples(ShortBuffer pcm)
        {
            if (this.handle == MemoryUtil.NULL)
            {
                return -1;
            }

            return STBVorbis.stb_vorbis_get_samples_short_interleaved(this.handle, this.getChannels(), pcm);
        }

        @Override
        public void close()
        {
            MemoryUtil.memFree(this.encodedAudio);

            if (this.handle != MemoryUtil.NULL)
            {
                STBVorbis.stb_vorbis_close(this.handle);
                this.handle = MemoryUtil.NULL;
            }
        }

        @Override
        public synchronized void seek(int sampleIndex)
        {
            STBVorbis.stb_vorbis_seek(this.handle, sampleIndex);
        }

        @Override
        public int getSampleOffset()
        {
            return STBVorbis.stb_vorbis_get_sample_offset(this.handle);
        }
    }
}
