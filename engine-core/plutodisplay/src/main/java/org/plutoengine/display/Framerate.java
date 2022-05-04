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
