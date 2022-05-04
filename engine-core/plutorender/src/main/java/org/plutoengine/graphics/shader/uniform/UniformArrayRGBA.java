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

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.plutoengine.util.color.IRGBA;

/**
 * A uniform allowing loading RGBA color arrays into shader uniforms.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class UniformArrayRGBA extends UniformVec4
{
    /**
     * Creates a new instance of the {@link UniformArrayRGBA} uniform
     * with the specified shader location.
     *
     * @param location The location within the shader
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public UniformArrayRGBA(int location)
    {
        super(location);
    }

    /**
     * Loads the {@link IRGBA} color components into the shader uniform.
     *
     * @param values The {@link IRGBA} color object array
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(IRGBA[] values)
    {
        final int dimensions = 4;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            var buf = stack.mallocFloat(dimensions * values.length);

            for (var value : values)
            {
                buf.put(value.red())
                   .put(value.green())
                   .put(value.blue())
                   .put(value.alpha());
            }

            buf.flip();

            GL33.glUniform4fv(this.location, buf);
        }
    }
}
