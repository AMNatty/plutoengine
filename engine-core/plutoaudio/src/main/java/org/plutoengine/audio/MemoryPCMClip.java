/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine.audio;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;

public class MemoryPCMClip extends RandomAccessClip
{
    private final ShortBuffer pcmAudio;

    MemoryPCMClip(Path path) throws IOException
    {
        long handle = MemoryUtil.NULL;
        ByteBuffer audioBytes = null;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            audioBytes = AudioLoader.loadIntoMemory(path);

            IntBuffer error = stack.mallocInt(1);
            handle = STBVorbis.stb_vorbis_open_memory(audioBytes, error, null);

            if (handle == MemoryUtil.NULL)
            {
                this.close();
                throw new IOException(String.format("Failed to load '%s', error code %d.\n", path, error.get(0)));
            }

            STBVorbisInfo info = STBVorbisInfo.malloc(stack);
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
    public int getLengthInSamples()
    {
        return this.samplesLength / this.getChannels();
    }

    @Override
    public int getSamples(ShortBuffer pcm, int offset, boolean loopRead)
    {
        int readTotal = 0;
        int read;

        do
        {
            int thisRemaining = this.pcmAudio.limit() - offset;
            read = Math.min(pcm.remaining() - readTotal, thisRemaining);
            pcm.put(pcm.position() + readTotal, this.pcmAudio, offset, read);
            readTotal += read;

            offset = (offset + read) % (this.getLengthInSamples() * this.channels);
        }
        while (loopRead && pcm.limit() - readTotal > 0);

        return readTotal / this.getChannels();
    }

    @Override
    public void close()
    {
        MemoryUtil.memFree(this.pcmAudio);
    }
}
