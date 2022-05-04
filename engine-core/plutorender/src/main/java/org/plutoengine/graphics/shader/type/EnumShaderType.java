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

package org.plutoengine.graphics.shader.type;

import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import org.plutoengine.graphics.gl.IOpenGLEnum;

public enum EnumShaderType implements IOpenGLEnum
{
    VERTEX(GL33.GL_VERTEX_SHADER),
    GEOMETRY(GL33.GL_GEOMETRY_SHADER),
    COMPUTE(GL43.GL_COMPUTE_SHADER),
    FRAGMENT(GL33.GL_FRAGMENT_SHADER),
    TESSELATION_CONTROL(GL40.GL_TESS_CONTROL_SHADER),
    TESSELATION_EVALUATION(GL40.GL_TESS_EVALUATION_SHADER);

    private final int id;

    EnumShaderType(int id)
    {
        this.id = id;
    }

    public int getGLID()
    {
        return this.id;
    }
}
