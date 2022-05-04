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

package org.plutoengine.graphics.vbo;

import org.lwjgl.opengl.GL33;

import java.nio.Buffer;

public sealed abstract class ArrayBuffer<T extends Buffer> implements AutoCloseable permits FloatArrayBuffer, IndexArrayBuffer, IntArrayBuffer
{
    protected int glID;

    protected final int type;
    private final int size;

    protected ArrayBuffer(int type, int size)
    {
        this.glID = GL33.glGenBuffers();
        this.type = type;
        this.size = size;
    }

    public abstract EnumArrayBufferType getDataType();

    public abstract void writePartialData(T data, long offset);

    public int getSize()
    {
        return this.size;
    }

    public void bind()
    {
        GL33.glBindBuffer(this.type, this.glID);
    }

    public void unbind()
    {
        GL33.glBindBuffer(this.type, 0);
    }

    public void close()
    {
        GL33.glDeleteBuffers(this.glID);

        this.glID = 0;
    }

    public int getID()
    {
        return this.glID;
    }
}
