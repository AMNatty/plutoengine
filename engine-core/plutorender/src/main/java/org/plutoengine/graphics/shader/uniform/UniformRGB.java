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

import org.plutoengine.util.color.IRGB;

/**
 * A uniform allowing loading RGBA color data into shader uniforms.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class UniformRGB extends UniformVec3
{
    /**
     * Creates a new instance of the {@link UniformRGB} uniform
     * with the specified shader location.
     *
     * @param location The location within the shader
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public UniformRGB(int location)
    {
        super(location);
    }

    /**
     /**
     * Loads the {@link IRGB} color components into the shader uniform.
     *
     * @param value The {@link IRGB} color object
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(IRGB value)
    {
        GL33.glUniform3f(this.location, value.red(), value.green(), value.blue());
    }

    /**
     * Loads the RGB color components into the shader uniform.
     *
     * @param r The red color component, in range [0..1]
     * @param g The green color component, in range [0..1]
     * @param b The blue color component, in range [0..1]
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(float r, float g, float b)
    {
        GL33.glUniform3f(this.location, r, g, b);
    }
}
