package cz.tefek.pluto.engine.audio;

public interface ISeekableAudioTrack extends IAudioStream
{
    int getSampleOffset();

    int getLengthInSamples();

    default void skip(int sampleCount)
    {
        this.seek(Math.min(Math.max(0, this.getSampleOffset() + sampleCount), this.getLengthInSamples()));
    }

    default void skipTo(float offset0to1)
    {
        this.seek(Math.round(this.getLengthInSamples() * offset0to1));
    }

    default void rewind()
    {
        this.seek(0);
    }

    void seek(int sampleIndex);
}
