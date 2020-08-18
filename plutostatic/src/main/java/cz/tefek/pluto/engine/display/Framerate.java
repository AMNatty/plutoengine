package cz.tefek.pluto.engine.display;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author 493msi
 *
 */
public class Framerate
{
    private static long lastDraw = 0;

    private static double frameTime = Double.NaN;

    private static double FPS = Double.NaN;

    private static int interpolatedFPS;

    private static boolean firstRemoved = false;

    private static Deque<Long> drawTimestamps = new LinkedBlockingDeque<>();

    public static double getFrameTime()
    {
        return frameTime;
    }

    public static double getFPS()
    {
        return FPS;
    }

    public static int getInterpolatedFPS()
    {
        return interpolatedFPS;
    }

    public static void tick()
    {
        var now = System.nanoTime();

        if (lastDraw > 0)
        {
            var frameTimeNs = now - lastDraw;
            frameTime = frameTimeNs / (double) TimeUnit.MILLISECONDS.toNanos(1);
            FPS = TimeUnit.SECONDS.toMillis(1) / frameTime;
        }

        var nowMs = System.currentTimeMillis();

        drawTimestamps.add(nowMs);

        Long oldestDraw;
        long oneSecondAgo = nowMs - 1000;

        while ((oldestDraw = drawTimestamps.peek()) != null && oldestDraw < oneSecondAgo)
        {
            drawTimestamps.remove();
            firstRemoved = true;
        }

        if (firstRemoved)
        {
            interpolatedFPS = drawTimestamps.size();
        }
        else
        {
            interpolatedFPS = (int) Math.round(FPS);
        }

        lastDraw = now;
    }
}
