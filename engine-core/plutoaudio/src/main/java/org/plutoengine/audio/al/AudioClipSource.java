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

package org.plutoengine.audio.al;

import org.plutoengine.audio.RandomAccessClip;

import java.nio.ShortBuffer;

public class AudioClipSource extends AudioDoubleBufferedSource
{
    private final RandomAccessClip clip;

    private final boolean looping;

    private int readHead = 0;

    public AudioClipSource(RandomAccessClip clip, boolean looping)
    {
        super(clip);

        this.clip = clip;
        this.looping = looping;
    }

    public AudioClipSource(RandomAccessClip clip)
    {
        this(clip, false);
    }

    @Override
    protected int getSamples(ShortBuffer pcmTransferBuf)
    {
        var read = this.clip.getSamples(pcmTransferBuf, this.readHead * this.channels, this.looping);
        this.readHead += read / this.channels;

        if (this.looping)
            this.readHead %= this.clip.getLengthInSamples();

        return read;
    }

    public boolean isLooping()
    {
        return this.looping;
    }
}
