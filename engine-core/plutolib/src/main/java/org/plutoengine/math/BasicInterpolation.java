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
