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

import org.plutoengine.graphics.vao.attrib.ReservedAttributes;
import org.plutoengine.graphics.shader.ShaderBase;
import org.plutoengine.graphics.shader.ShaderProgram;
import org.plutoengine.graphics.shader.VertexArrayAttribute;
import org.plutoengine.graphics.shader.uniform.Uniform;
import org.plutoengine.graphics.shader.uniform.UniformMat3x2;
import org.plutoengine.graphics.shader.uniform.UniformMat4;
import org.plutoengine.graphics.shader.uniform.UniformVec2;
import org.plutoengine.graphics.shader.uniform.UniformVec4;
import org.plutoengine.graphics.shader.uniform.auto.AutoViewportProjection;

/**
 * @author 493msi
 *
 */
@ShaderProgram
public final class Shader2D extends ShaderBase implements IShader2D
{
    @AutoViewportProjection
    @Uniform(name = "projection")
    public UniformMat4 projectionMatrix;

    @Uniform(name = "transformation")
    public UniformMat3x2 transformationMatrix;

    @Uniform
    public UniformVec2 uvBase;

    @Uniform
    public UniformVec2 uvDelta;

    @Uniform
    public UniformVec4 recolor;

    @VertexArrayAttribute(ReservedAttributes.POSITION)
    public int position;

    @VertexArrayAttribute(ReservedAttributes.UV)
    public int uvCoords;

    @Override
    public void loadUV(float uvStartX, float uvStartY, float uWidth, float vHeight)
    {
        this.uvBase.load(uvStartX, uvStartY);
        this.uvDelta.load(uWidth, vHeight);
    }

    @Override
    public void loadRecolor(float r, float g, float b, float a)
    {
        this.recolor.load(r, g, b, a);
    }

    @Override
    public void loadProjectionMatrix(Matrix4fc matrix)
    {
        this.projectionMatrix.load(matrix);
    }

    @Override
    public void loadTransformationMatrix(Matrix3x2fc matrix)
    {
        this.transformationMatrix.load(matrix);
    }
}
