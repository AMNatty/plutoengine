package cz.tefek.srclone.util;

public class AngleUtil
{
    public static float within180(float angle)
    {
        angle %= 2.0 * Math.PI;

        if (angle > Math.PI)
            angle -= 2 * Math.PI;

        if (angle < -Math.PI)
            angle += 2 * Math.PI;

        return angle;
    }
}
