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
 * A class to generate a cubic bezier interpolation function. This
 * implementation creates a lookup table, so create it one and then
 * use it with basically no speed penalty.
 * 
 * @since 0.2
 * @author 493msi
 */
public class CubicBezierLT
{
    private static final int tableSize = 1 << 11;
    private final double[] values;
    private final double upperBound = tableSize - 1.0;

    /**
     * Creates a new {@code CubicBezierLT} from the given parameters.
     * 
     * @param cpx1 the X position of the direction the function "steers towards"
     * @param cpy1 the Y position of the direction the function "steers towards"
     * @param cpx2 the X position of the direction the function "arrives from"
     * @param cpy2 the Y position of the direction the function "arrives from"
     * 
     * @since 0.3
     * @author 493msi
     */
    public CubicBezierLT(double cpx1, double cpy1, double cpx2, double cpy2)
    {
        if (cpx1 < 0 || cpx1 > 1 || cpx2 < 0 || cpx2 > 1)
        {
            throw new IllegalArgumentException("Parameter out of range, only 0..1 is supported (only one Y value in 0..1 for each X in 0..1).");
        }

        var cubicBezier = new CubicBezier(cpx1, cpy1, cpx2, cpy2);

        this.values = new double[tableSize];

        for (int i = 0; i < tableSize; i++)
        {
            this.values[i] = cubicBezier.forX(i / this.upperBound);
        }
    }

    /**
     * Retrieves the approximate value for the given x and the supplied
     * parameters in the constructor.
     * 
     * @param xIn the input X position
     * 
     * @return the approximate Y position for the given X position
     * 
     * @since 0.3
     * @author 493msi
     */
    public double forX(double xIn)
    {
        if (xIn < 0)
        {
            return this.forX(0);
        }

        if (xIn > 1)
        {
            return this.forX(1);
        }

        return this.values[(int) (xIn * this.upperBound)];
    }
}
