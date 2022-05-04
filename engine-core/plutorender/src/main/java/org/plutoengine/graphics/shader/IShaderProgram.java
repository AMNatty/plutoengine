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

package org.plutoengine.graphics.shader;

import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.shader.type.IShader;

public interface IShaderProgram extends AutoCloseable
{
    int getID();

    default void attach(IShader shader)
    {
        GL33.glAttachShader(this.getID(), shader.getID());
    }

    default void detach(IShader shader)
    {
        GL33.glDetachShader(this.getID(), shader.getID());
    }

    default void start()
    {
        GL33.glUseProgram(this.getID());
    }

    default void stop()
    {
        GL33.glUseProgram(0);
    }

    default void close()
    {
        this.stop();
        GL33.glDeleteProgram(this.getID());
    }
}
