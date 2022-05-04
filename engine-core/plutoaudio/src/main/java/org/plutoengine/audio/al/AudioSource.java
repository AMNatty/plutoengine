/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
