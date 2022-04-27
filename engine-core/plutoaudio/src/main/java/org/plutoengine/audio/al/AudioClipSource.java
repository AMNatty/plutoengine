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
