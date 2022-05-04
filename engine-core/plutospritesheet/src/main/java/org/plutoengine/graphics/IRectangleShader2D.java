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

package org.plutoengine.graphics;

import org.joml.Matrix3x2fc;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector4fc;

import org.plutoengine.graphics.shader.IShaderProgram;

public interface IRectangleShader2D extends IShaderProgram
{
    void loadProjectionMatrix(Matrix4fc matrix);

    void loadTransformationMatrix(Matrix3x2fc matrix);

    default void loadUV(Vector2fc uvBase, Vector2fc uvDelta)
    {
        this.loadUV(uvBase.x(), uvBase.y(), uvDelta.x(), uvDelta.y());
    }

    void loadUV(float uBase, float yBase, float uWidth, float vHeight);

    default void loadRecolor(Vector4fc col)
    {
        this.loadRecolor(col.x(), col.y(), col.z(), col.w());
    }

    void loadRecolor(float r, float g, float b, float a);
}
