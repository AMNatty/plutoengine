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

import org.joml.Matrix4f;

public class ProjectionMatrix
{
    /**
     * Create a 2D orthogonal projection Matrix4f based on the width and height.
     * 
     * @param width  The ortho width
     * @param height The ortho height
     * 
     * @return the matrix
     * 
     * @author 493msi
     * @since 0.1
     */
    public static Matrix4f createOrtho2D(int width, int height)
    {
        var orthoMatrix = new Matrix4f();
        orthoMatrix.setOrtho2D(0, width, height, 0);

        return orthoMatrix;
    }

    /**
     * Create a centered 2D orthogonal projection Matrix4f based on the width and
     * height.
     * 
     * @param width  The ortho width
     * @param height The ortho height
     * 
     * @return the matrix
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix4f createOrtho2DCentered(int width, int height)
    {
        var orthoMatrix = new Matrix4f();
        orthoMatrix.setOrtho2D(width / 2.0f, width / 2.0f, height / 2.0f, height / 2.0f);

        return orthoMatrix;
    }

    /**
     * Create a perspective frustum based on the parameters.
     * 
     * @param aspectRatio The aspect ratio of the frustum
     * @param fov         The fov of the frustum
     * @param zNear       The distance of the zNear clipping plane
     * @param zFar        The distance of the zFar clipping plane
     * 
     * @return the perspective matrix
     * 
     * @author 493msi
     * @since 0.1
     */
    public static Matrix4f createPerspective(float aspectRatio, float fov, float zNear, float zFar)
    {
        var perspective = new Matrix4f();
        perspective.setPerspective(fov, aspectRatio, zNear, zFar);

        return perspective;
    }
}
