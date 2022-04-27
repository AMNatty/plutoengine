package org.plutoengine.audio;

public interface IAudio extends AutoCloseable
{
    int getSampleRate();

    int getChannels();

    void close();
}
