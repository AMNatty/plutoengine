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

import org.lwjgl.openal.AL11;

import java.util.Arrays;

public enum EnumDistanceModel implements IOpenALEnum
{
    NONE(AL11.AL_NONE),
    INVERSE_DISTANCE(AL11.AL_INVERSE_DISTANCE),
    INVERSE_DISTANCE_CLAMPED(AL11.AL_INVERSE_DISTANCE_CLAMPED),
    LINEAR_DISTANCE(AL11.AL_LINEAR_DISTANCE),
    LINEAR_DISTANCE_CLAMPED(AL11.AL_LINEAR_DISTANCE_CLAMPED),
    EXPONENT_DISTANCE(AL11.AL_EXPONENT_DISTANCE),
    EXPONENT_DISTANCE_CLAMPED(AL11.AL_EXPONENT_DISTANCE_CLAMPED);

    private final int alID;

    EnumDistanceModel(int alID)
    {
        this.alID = alID;
    }

    public static EnumDistanceModel getByID(int id)
    {
        return Arrays.stream(EnumDistanceModel.values()).filter(model -> model.getALID() == id).findAny().orElse(null);
    }

    @Override
    public int getALID()
    {
        return this.alID;
    }
}
