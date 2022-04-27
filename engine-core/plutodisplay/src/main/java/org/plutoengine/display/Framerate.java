package org.plutoengine.display;

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

    private static double animationTimer = 0;

    private static double fps = Double.NaN;

    private static int interpolatedFPS;

    private static boolean firstRemoved = false;

    private static final Deque<Long> drawTimestamps = new LinkedBlockingDeque<>();

    public static double getFrameTime()
    {
        return frameTime;
    }

    public static double getFPS()
    {
        return fps;
    }

    public static int getInterpolatedFPS()
    {
        return interpolatedFPS;
    }

    public static float getAnimationTimer()
    {
        return (float) animationTimer;
    }

    public static void tick()
    {
        var now = System.nanoTime();

        if (lastDraw > 0)
        {
            var frameTimeNs = now - lastDraw;
            frameTime = frameTimeNs / (double) TimeUnit.MILLISECONDS.toNanos(1);
            animationTimer += frameTimeNs / (double) TimeUnit.SECONDS.toNanos(1);
            // Maintain precision in case the engine runs for many hours
            animationTimer %= TimeUnit.DAYS.toMinutes(1);
            fps = TimeUnit.SECONDS.toMillis(1) / frameTime;
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
            interpolatedFPS = (int) Math.round(fps);
        }

        lastDraw = now;
    }
}
