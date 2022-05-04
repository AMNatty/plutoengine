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

/**
 * A clamped sine wave generator, for animations, mostly.
 * 
 * @since 0.3
 * @author 493msi
 */
public class ClampedSineWave
{
    /**
     * Gets a clamped value of the abs(sine) function from the given parameters
     * as a {@link double}.
     * 
     * @param animationProgress The animation progress in frames.
     * @param frameOffset The animation offset in frames.
     * @param animationFrames The total amount of animation frames.
     * @param bottomClamp The sine wave clamp, minimum value.
     * @param topClamp The sine wave clamp, maximum value.
     * 
     * @return The resulting value
     * 
     * @since 0.3
     * @author 493msi
     */
    public static double getAbsolute(double animationProgress, double frameOffset, double animationFrames, double bottomClamp, double topClamp)
    {
        var actualProgress = (animationProgress + frameOffset) / animationFrames;
        return Math.min(Math.max(Math.abs(Math.sin(actualProgress * Math.PI)), bottomClamp), topClamp);
    }

    /**
     * Gets a clamped value of the sine function from the given parameters as a
     * {@link double}.
     * 
     * @param animationProgress The animation progress in frames.
     * @param frameOffset The animation offset in frames.
     * @param animationFrames The total amount of animation frames.
     * @param bottomClamp The sine wave clamp, minimum value.
     * @param topClamp The sine wave clamp, maximum value.
     * 
     * @return The resulting value
     * 
     * @since 0.3
     * @author 493msi
     */
    public static double get(double animationProgress, double frameOffset, double animationFrames, float bottomClamp, float topClamp)
    {
        var actualProgress = (animationProgress + frameOffset) / animationFrames;
        return Math.min(Math.max(Math.sin(actualProgress * Math.PI), bottomClamp), topClamp);
    }

    /**
     * Gets a clamped value of the sine function from the given parameters as a
     * {@link double}.
     * 
     * @param animationProgress The animation progress in frames.
     * @param animationFrames The total amount of animation frames.
     * @param bottomClamp The sine wave clamp, minimum value.
     * @param topClamp The sine wave clamp, maximum value.
     * 
     * @return The resulting value
     * 
     * @since 0.3
     * @author 493msi
     */
    public static double get(double animationProgress, double animationFrames, float bottomClamp, float topClamp)
    {
        var progress = animationProgress / animationFrames;
        return Math.min(Math.max(Math.sin(progress * Math.PI), bottomClamp), topClamp);
    }
}
