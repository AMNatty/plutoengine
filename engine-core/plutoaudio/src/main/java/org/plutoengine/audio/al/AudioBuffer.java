package org.plutoengine.audio.al;

import org.lwjgl.openal.AL10;

import java.nio.ShortBuffer;

class AudioBuffer implements AutoCloseable
{
    private final int id;
    private final int format;
    private final int sampleRate;

    AudioBuffer(int id, int format, int sampleRate)
    {
        this.id = id;
        this.format = format;
        this.sampleRate = sampleRate;
    }

    public int getID()
    {
        return this.id;
    }

    public void writeData(ShortBuffer data)
    {
        AL10.alBufferData(this.id, this.format, data, this.sampleRate);
    }

    @Override
    public void close()
    {
        AL10.alDeleteBuffers(this.id);
    }
}
