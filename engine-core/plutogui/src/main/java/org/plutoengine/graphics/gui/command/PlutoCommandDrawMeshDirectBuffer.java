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

package org.plutoengine.graphics.gui.command;

import org.lwjgl.system.MemoryUtil;
import org.plutoengine.graphics.vao.attrib.AttributeInfo;
import org.plutoengine.graphics.vbo.EnumArrayBufferType;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class PlutoCommandDrawMeshDirectBuffer extends PlutoCommandDrawMesh implements AutoCloseable
{
    public void addIndices(IntBuffer data)
    {
        if (data == null)
            return;

        if (this.indices == null)
        {
            if (data.isDirect())
            {
                this.indices = data;
                var size = this.indices.remaining();
                this.indices.limit(size);
                this.indices.position(size);
                return;
            }
            else
            {
                this.indices = MemoryUtil.memAllocInt(data.remaining());
            }
        }

        if (this.indices.remaining() < data.remaining())
        {
            var newSize = Math.max(this.indices.capacity() << 1, this.indices.capacity() + data.remaining());
            this.indices = MemoryUtil.memRealloc(this.indices, newSize);
        }

        this.indices.put(data);

        if (data.isDirect())
            MemoryUtil.memFree(data);
    }

    public void addAttribute(int attrib, FloatBuffer data, int dimensions)
    {
        if (data == null)
            return;

        var meta = this.attributeInfo.computeIfAbsent(attrib, k -> new AttributeInfo(EnumArrayBufferType.FLOAT, attrib, dimensions));

        if (meta.dimensions() != dimensions)
            throw new IllegalArgumentException("Attribute dimensions mismatch!");

        this.data.compute(attrib, (k, v) -> {
            if (v == null)
            {
                if (data.isDirect())
                {
                    var size = data.remaining();
                    data.limit(size);
                    data.position(size);
                    return data;
                }
                else
                {
                    return MemoryUtil.memAllocFloat(data.remaining()).put(data);
                }
            }

            if (!(v instanceof FloatBuffer fab))
                throw new IllegalArgumentException();

            if (data.remaining() > fab.remaining())
            {
                var newSize = Math.max(fab.capacity() << 1, fab.capacity() + data.remaining());
                fab = MemoryUtil.memRealloc(fab, newSize);
            }

            fab.put(data);

            if (data.isDirect())
                MemoryUtil.memFree(data);

            return fab;
        });
    }


    public void addAttribute(int attrib, IntBuffer data, int dimensions)
    {
        if (data == null)
            return;

        var meta = this.attributeInfo.computeIfAbsent(attrib, k -> new AttributeInfo(EnumArrayBufferType.INT, attrib, dimensions));

        if (meta.dimensions() != dimensions)
            throw new IllegalArgumentException("Attribute dimensions mismatch!");

        this.data.compute(attrib, (k, v) -> {
            if (v == null)
            {
                if (data.isDirect())
                {
                    var size = data.remaining();
                    data.limit(size);
                    data.position(size);
                    return data;
                }
                else
                {
                    return MemoryUtil.memAllocInt(data.remaining()).put(data);
                }
            }

            if (!(v instanceof IntBuffer iab))
                throw new IllegalArgumentException();

            if (data.remaining() > iab.remaining())
            {
                var newSize = Math.max(iab.capacity() << 1, iab.capacity() + data.remaining());
                iab = MemoryUtil.memRealloc(iab, newSize);
            }

            iab.put(data);

            if (data.isDirect())
                MemoryUtil.memFree(data);

            return iab;
        });
    }

    @Override
    public void close()
    {
        MemoryUtil.memFree(this.indices);

        this.data.values()
                 .forEach(MemoryUtil::memFree);
    }
}
