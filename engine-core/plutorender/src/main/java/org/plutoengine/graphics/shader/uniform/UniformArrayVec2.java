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

import org.joml.Vector2fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public class UniformArrayVec2 extends UniformBase
{
    public UniformArrayVec2(int location)
    {
        super(location);
    }

    public void load(Vector2fc... values)
    {
        final int dimensions = 2;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            var buf = stack.mallocFloat(dimensions * values.length);

            int i;
            for (i = 0; i < values.length; i++)
                values[i].get(i * dimensions, buf);

            buf.position(i * dimensions);

            buf.flip();

            GL33.glUniform2fv(this.location, buf);
        }
    }
}
