package org.plutoengine.audio.al;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.SOFTDirectChannels;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.audio.IAudioStream;
import org.plutoengine.audio.ISeekableAudioTrack;

import java.nio.ShortBuffer;

public class AudioTrack extends AudioSource
{
    private static final int BUFFER_SIZE_PER_CHANNEL = 16384;

    private final IAudioStream track;

    private final int format;

    private static final int DOUBLE_BUFFER = 2;

    private final int[] buffers;

    private boolean closeOnFinish;

    private final ShortBuffer pcm;

    public AudioTrack(IAudioStream track)
    {
        this.track = track;

        this.format = switch (track.getChannels()) {
            case 1 -> AL10.AL_FORMAT_MONO16;
            case 2 -> AL10.AL_FORMAT_STEREO16;
            default -> throw new UnsupportedOperationException("Unsupported number of channels: " + track.getChannels());
        };

        int bufferSize = track.getChannels() * BUFFER_SIZE_PER_CHANNEL;

        this.pcm = MemoryUtil.memAllocShort(bufferSize);

        this.buffers = new int[DOUBLE_BUFFER];
        AL10.alGenBuffers(this.buffers);

        AL10.alSourcei(this.source, SOFTDirectChannels.AL_DIRECT_CHANNELS_SOFT, AL10.AL_TRUE);
    }

    @Override
    public void close()
    {
        AL10.alDeleteBuffers(this.buffers);
        AL10.alDeleteSources(this.source);

        MemoryUtil.memFree(this.pcm);
    }

    public boolean play()
    {
        if (this.track instanceof ISeekableAudioTrack seekableAudioTrack)
            seekableAudioTrack.rewind();

        for (int buf : this.buffers)
        {
            this.stream(buf);
        }

        AL10.alSourcePlay(this.source);

        return true;
    }

    public void setCloseOnFinish()
    {
        this.closeOnFinish = true;
    }

    private void stream(int buffer)
    {
        this.pcm.clear();
        int samplesPerChannel = this.track.getSamples(this.pcm);

        if (samplesPerChannel == 0)
            return;

        var samples = samplesPerChannel * this.track.getChannels();
        this.pcm.limit(samples);
        AL10.alBufferData(buffer, this.format, this.pcm, this.track.getSampleRate());
        AL10.alSourceQueueBuffers(this.source, buffer);
    }

    public boolean update()
    {
        int processed = AL10.alGetSourcei(this.source, AL10.AL_BUFFERS_PROCESSED);

        for (int i = 0; i < processed; i++)
        {
            int buffer = AL10.alSourceUnqueueBuffers(this.source);
            this.stream(buffer);
        }

        if (AL10.alGetSourcei(this.source, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED)
        {
            if (this.closeOnFinish)
            {
                this.close();
            }

            return false;
        }

        return true;
    }

}
