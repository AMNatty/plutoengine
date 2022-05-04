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
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public non-sealed class IntArrayBuffer extends ArrayBuffer<IntBuffer>
{
    private IntArrayBuffer(int size)
    {
        super(GL33.GL_ARRAY_BUFFER, size);
    }

    @Override
    public void writePartialData(IntBuffer data, long offset)
    {
        GL33.glBufferSubData(this.type, offset, data);
    }

    @Override
    public EnumArrayBufferType getDataType()
    {
        return EnumArrayBufferType.INT;
    }

    public static IntArrayBuffer from(IntBuffer data)
    {
        if (!data.isDirect())
        {
            var ib = MemoryUtil.memAllocInt(data.remaining());
            ib.put(data);
            ib.flip();
            var iab = from(ib);
            MemoryUtil.memFree(ib);
            return iab;
        }

        var iab = new IntArrayBuffer(data.remaining());
        iab.bind();
        GL33.glBufferData(iab.type, data, GL33.GL_STATIC_DRAW);
        return iab;
    }

    public static IntArrayBuffer from(int[] data)
    {
        var iab = new IntArrayBuffer(data.length);
        iab.bind();
        GL33.glBufferData(iab.type, data, GL33.GL_STATIC_DRAW);
        return iab;
    }

    public static IntArrayBuffer empty(int size)
    {
        var iab = new IntArrayBuffer(size);
        iab.bind();
        GL33.glBufferData(iab.type, size, GL33.GL_DYNAMIC_DRAW);
        return iab;
    }
}
