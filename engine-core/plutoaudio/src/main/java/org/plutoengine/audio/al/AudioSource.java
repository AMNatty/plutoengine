package org.plutoengine.audio.al;

import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.joml.Vector3fc;
import org.lwjgl.openal.AL10;

public abstract class AudioSource implements AutoCloseable
{
    protected final int id;
    protected Vector3fc position;

    protected AudioSource()
    {
        this.id = AL10.alGenSources();
    }

    @MustBeInvokedByOverriders
    public boolean play()
    {
        AL10.alSourcePlay(this.id);
        return true;
    }

    @MustBeInvokedByOverriders
    public void pause()
    {
        AL10.alSourcePause(this.id);
    }

    @MustBeInvokedByOverriders
    public void stop()
    {
        AL10.alSourceStop(this.id);
    }

    @MustBeInvokedByOverriders
    public void close()
    {
        AL10.alDeleteSources(this.id);
    }

    @MustBeInvokedByOverriders
    public void position(AudioContext context, Vector3fc pos)
    {
        this.position = pos;
        var tPos = context.transform(pos);
        AL10.alSource3f(this.id, AL10.AL_POSITION, tPos.x(), tPos.y(), tPos.z());
    }

    public Vector3fc getPosition()
    {
        return this.position;
    }

    @MustBeInvokedByOverriders
    public void velocity(AudioContext context, Vector3fc velocity)
    {
        var tVelocity = context.transform(velocity);
        AL10.alSource3f(this.id, AL10.AL_VELOCITY, tVelocity.x(), tVelocity.y(), tVelocity.z());
    }

    @MustBeInvokedByOverriders
    public void pitch(float f)
    {
        AL10.alSourcef(this.id, AL10.AL_PITCH, f);
    }

    @MustBeInvokedByOverriders
    public void volume(float f)
    {
        AL10.alSourcef(this.id, AL10.AL_GAIN, f);
    }
}
