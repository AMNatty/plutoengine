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

package org.plutoengine.graphics.shader.ubo;

import org.lwjgl.opengl.GL33;

import org.plutoengine.graphics.shader.ShaderBase;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public final class UniformBufferObject
{
    private final int id;
    private final long size;
    private final String name;

    public UniformBufferObject(long size, String name)
    {
        this.id = GL33.glGenBuffers();
        this.size = size;
        this.name = name;

        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, this.id);
        GL33.glBufferData(GL33.GL_UNIFORM_BUFFER, this.size, GL33.GL_DYNAMIC_DRAW);
        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, 0);

        GL33.glBindBufferRange(GL33.GL_UNIFORM_BUFFER, 0, this.id, 0, this.size);
    }

    public void bindLocation(ShaderBase shader, int bindingPoint)
    {
        var programID = shader.getID();

        int id = GL33.glGetUniformBlockIndex(programID, this.name);

        if (id == GL33.GL_INVALID_INDEX)
        {
            Logger.logf(SmartSeverity.ERROR, "Uniform block %s not found in shader %d.\n", this.name, programID);
            GL33.glUseProgram(0);
            return;
        }

        GL33.glUniformBlockBinding(programID, id, bindingPoint);
    }

    public void writeIntData(long offset, int[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void writeLongData(long offset, long[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void writeFloatData(long offset, float[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void writeDoubleData(long offset, double[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public long getSize()
    {
        return this.size;
    }

    public void bind()
    {
        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, this.id);
    }

    public void unbind()
    {
        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, 0);
    }

    public void free()
    {
        GL33.glDeleteBuffers(this.id);
    }
}
