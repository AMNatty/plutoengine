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

import org.joml.Matrix3x2f;

/**
 * A class with factory methods for the most used view transformations.
 * 
 * @author 493msi
 * @since 0.3
 */
public class ViewMatrix
{
    /**
     * Create a 2D view matrix.
     * 
     * @param x The X camera translation
     * @param y The Y camera translation
     * @param zoom The zoom
     * 
     * @return the view matrix
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix3x2f createView(int x, int y, float zoom)
    {
        var view = new Matrix3x2f();
        view.scale(zoom);
        view.translate(x, y);

        return view;
    }
}
