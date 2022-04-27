package org.plutoengine.audio;

public interface ISeekableTrack extends IStreamingAudio
{
    void seek(int sampleIndex);

    default void rewind()
    {
        this.seek(0);
    }
}
