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

import java.nio.FloatBuffer;

public final class FloatArrayBuffer extends ArrayBuffer<FloatBuffer>
{
    private FloatArrayBuffer(int size)
    {
        super(GL33.GL_ARRAY_BUFFER, size);
    }

    @Override
    public void writePartialData(FloatBuffer data, long offset)
    {
        GL33.glBufferSubData(this.type, offset, data);
    }

    @Override
    public EnumArrayBufferType getDataType()
    {
        return EnumArrayBufferType.FLOAT;
    }

    public static FloatArrayBuffer from(FloatBuffer data)
    {
        if (!data.isDirect())
        {
            var fb = MemoryUtil.memAllocFloat(data.remaining());
            fb.put(data);
            fb.flip();
            var fab = from(fb);
            MemoryUtil.memFree(fb);
            return fab;
        }

        var fab = new FloatArrayBuffer(data.remaining());
        fab.bind();
        GL33.glBufferData(fab.type, data, GL33.GL_STATIC_DRAW);
        return fab;
    }

    public static FloatArrayBuffer from(float[] data)
    {
        var fab = new FloatArrayBuffer(data.length);
        fab.bind();
        GL33.glBufferData(fab.type, data, GL33.GL_STATIC_DRAW);
        return fab;
    }

    public static FloatArrayBuffer empty(int size)
    {
        var fab = new FloatArrayBuffer(size);
        fab.bind();
        GL33.glBufferData(fab.type, size, GL33.GL_STATIC_DRAW);
        return fab;
    }
}
