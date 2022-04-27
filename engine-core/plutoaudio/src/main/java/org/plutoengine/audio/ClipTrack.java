package org.plutoengine.audio;

public abstract class ClipTrack extends SeekableTrack implements ISeekableClip
{
    protected int samplesLength;

    @Override
    public int getLengthInSamples()
    {
        return this.samplesLength;
    }
}
