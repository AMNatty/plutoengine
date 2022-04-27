package org.plutoengine.audio;

import java.nio.ShortBuffer;

public interface IStreamingAudio extends IAudio
{
    int getSamples(ShortBuffer pcm);

    int getSampleOffset();
}
