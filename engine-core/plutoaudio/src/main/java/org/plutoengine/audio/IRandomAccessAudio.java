package org.plutoengine.audio;

import java.nio.ShortBuffer;

public interface IRandomAccessAudio extends IClip
{
    int getSamples(ShortBuffer pcm, int offset, boolean loopRead);
}
