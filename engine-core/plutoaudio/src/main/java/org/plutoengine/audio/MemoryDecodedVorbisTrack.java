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
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Path;

public class MemoryDecodedVorbisTrack extends ClipTrack
{
    protected long handle;

    private final ByteBuffer encodedAudio;

    MemoryDecodedVorbisTrack(Path path) throws IOException
    {
        try
        {
            this.encodedAudio = AudioLoader.loadIntoMemory(path);
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
                throw new IOException(String.format("Failed to load '%s', error code %d.\n", path, error.get(0)));
            }

            STBVorbisInfo info = STBVorbisInfo.malloc(stack);
            STBVorbis.stb_vorbis_get_info(this.handle, info);

            this.channels = info.channels();
            this.sampleRate = info.sample_rate();
        }

        this.samplesLength = STBVorbis.stb_vorbis_stream_length_in_samples(this.handle);

        Logger.logf(SmartSeverity.AUDIO, """
            \tSample rate:\t%d
            \t\tChannels:\t%d
            \t\tSamples:\t%d
            %n""", this.sampleRate, this.channels, this.samplesLength);
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
