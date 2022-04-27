package org.plutoengine.audio.al;

import org.plutoengine.audio.ISeekableTrack;

import java.nio.ShortBuffer;

public class AudioTrackSource extends AudioDoubleBufferedSource
{
    private final ISeekableTrack track;

    public AudioTrackSource(ISeekableTrack track)
    {
        super(track);

        this.track = track;
    }

    public boolean play()
    {
        this.track.rewind();

        return super.play();
    }

    @Override
    protected int getSamples(ShortBuffer pcmTransferBuf)
    {
        return this.track.getSamples(pcmTransferBuf);
    }
}
