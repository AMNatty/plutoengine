package org.plutoengine.audio;

public interface ISeekableClip extends ISeekableTrack, IClip
{
    default void skip(int sampleCount)
    {
        this.seek(Math.min(Math.max(0, this.getSampleOffset() + sampleCount), this.getLengthInSamples()));
    }

    default void skipTo(float offset0to1)
    {
        this.seek(Math.round(this.getLengthInSamples() * offset0to1));
    }
}
