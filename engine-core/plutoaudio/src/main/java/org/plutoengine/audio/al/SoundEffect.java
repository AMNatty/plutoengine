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

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;
import org.plutoengine.audio.RandomAccessClip;

import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

public class SoundEffect
{
    private final @NotNull RandomAccessClip clip;
    private @NotNull Vector3fc position;
    private float volume;
    private float pitch;
    private UnaryOperator<Vector3fc> movementMapper;
    private BooleanSupplier keepAliveFunction;

    public SoundEffect(@NotNull RandomAccessClip soundEffect, @NotNull Vector3fc position, float volume)
    {
        this.clip = soundEffect;
        this.position = position;
        this.volume = volume;
        this.pitch = 1.0f;
    }

    public SoundEffect position(Vector3fc position)
    {
        this.position = position;
        return this;
    }

    public SoundEffect volume(float volume)
    {
        this.volume = volume;
        return this;
    }

    public SoundEffect pitch(float pitch)
    {
        this.pitch = pitch;
        return this;
    }

    public SoundEffect movementMapper(UnaryOperator<Vector3fc> movementMapper)
    {
        this.movementMapper = movementMapper;
        return this;
    }

    public SoundEffect keepAliveFunction(BooleanSupplier keepAliveFunction)
    {
        this.keepAliveFunction = keepAliveFunction;
        return this;
    }

    @NotNull RandomAccessClip getClip()
    {
        return this.clip;
    }

    @NotNull Vector3fc getPosition()
    {
        return this.position;
    }

    float getVolume()
    {
        return this.volume;
    }

    float getPitch()
    {
        return this.pitch;
    }

    UnaryOperator<Vector3fc> getMovementMapper()
    {
        return this.movementMapper;
    }

    BooleanSupplier getKeepAliveFunction()
    {
        return this.keepAliveFunction;
    }
}
