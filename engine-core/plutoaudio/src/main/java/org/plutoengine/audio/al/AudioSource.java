package org.plutoengine.audio.al;

import org.joml.Vector3fc;
import org.lwjgl.openal.AL10;

public abstract class AudioSource implements AutoCloseable
{
    protected final int source;

    protected AudioSource()
    {
        this.source = AL10.alGenSources();
    }

    public void stop()
    {
        AL10.alSourceStop(this.source);
    }

    public void close()
    {
        this.stop();
        AL10.alDeleteSources(this.source);
    }

    public void position(Vector3fc pos)
    {
        AL10.alSource3f(this.source, AL10.AL_POSITION, pos.x(), pos.y(), pos.z());
    }

    public void velocity(Vector3fc velocity)
    {
        AL10.alSource3f(this.source, AL10.AL_VELOCITY, velocity.x(), velocity.y(), velocity.z());
    }

    public void pitch(float f)
    {
        AL10.alSourcef(this.source, AL10.AL_PITCH, f);
    }

    public void volume(float f)
    {
        AL10.alSourcef(this.source, AL10.AL_GAIN, f);
    }
}
