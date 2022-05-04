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

package org.plutoengine.graphics.shader.uniform;

import org.joml.Matrix3x2fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class UniformArrayMat3x2 extends UniformVec2
{
    public UniformArrayMat3x2(int programID)
    {
        super(programID);
    }

    public void load(Matrix3x2fc... matrices)
    {
        final int dimensions = 3 * 2;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer buf = stack.mallocFloat(dimensions * matrices.length);

            for (int i = 0; i < matrices.length; i++)
            {
                buf.position(i * dimensions);
                matrices[i].get(buf);
            }

            buf.flip();

            GL33.glUniformMatrix3x2fv(this.location, false, buf);
        }
    }
}
