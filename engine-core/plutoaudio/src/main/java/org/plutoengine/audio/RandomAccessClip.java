package org.plutoengine.audio;

public abstract class RandomAccessClip extends Track implements IRandomAccessAudio, IClip
{
    protected int samplesLength;

    @Override
    public int getLengthInSamples()
    {
        return this.samplesLength;
    }
}
