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

package org.plutoengine.math;

import org.joml.Math;

public class BasicInterpolation
{
    public static int roundedLerp(float value, int min, int max)
    {
        return min + Math.round(value * (max - min));
    }

    public static int roundedLerpWrap(float value, int min, int max)
    {
        var range = max - min;
        return min + (roundedLerp(value, 0, range) % range + range) % range;
    }

    public static int roundedLerp(double value, int min, int max)
    {
        return (int) (min + Math.round(value * (max - min)));
    }

    public static int roundedLerpWrap(double value, int min, int max)
    {
        var range = max - min;
        return min + (roundedLerp(value, 0, range) % range + range) % range;
    }

    public static int floorLerp(float value, int min, int max)
    {
        return (int) (min + Math.floor(value * (max - min)));
    }

    public static int floorLerpWrap(float value, int min, int max)
    {
        var range = max - min;
        return min + (floorLerp(value, 0, range) % range + range) % range;
    }

    public static int floorLerp(double value, int min, int max)
    {
        return (int) (min + Math.floor(value * (max - min)));
    }

    public static int floorLerpWrap(double value, int min, int max)
    {
        var range = max - min;
        return min + (floorLerp(value, 0, range) % range + range) % range;
    }
}
