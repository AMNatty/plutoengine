package org.plutoengine.audio;

import java.nio.ShortBuffer;

public interface IAudioStream
{
    int getSamples(ShortBuffer pcm);

    int getSampleRate();

    int getChannels();

    void close();
}
