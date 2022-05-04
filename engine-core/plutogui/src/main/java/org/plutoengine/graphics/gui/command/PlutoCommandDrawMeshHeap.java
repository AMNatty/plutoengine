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

import org.plutoengine.graphics.vao.attrib.AttributeInfo;
import org.plutoengine.graphics.vbo.EnumArrayBufferType;
import org.plutoengine.libra.command.impl.LiCommand;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class PlutoCommandDrawMeshHeap extends PlutoCommandDrawMesh
{
    public void addIndices(IntBuffer data)
    {
        if (data == null)
            return;

        if (this.indices == null)
            this.indices = IntBuffer.allocate(data.remaining());

        if (this.indices.remaining() < data.remaining())
            this.indices = IntBuffer.allocate(Math.max(this.indices.capacity() << 1, this.indices.capacity() + data.remaining())).put(this.indices.flip());

        this.indices.put(data);
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
                return FloatBuffer.allocate(data.remaining()).put(data);

            if (!(v instanceof FloatBuffer fab))
                throw new IllegalArgumentException();

            if (data.remaining() <= fab.remaining())
                return fab.put(data);

            var newBuf = FloatBuffer.allocate(Math.max(v.capacity() << 1, v.capacity() + data.remaining()));
            return newBuf.put(fab.flip()).put(data);
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
                return IntBuffer.allocate(data.remaining()).put(data);

            if (!(v instanceof IntBuffer iab))
                throw new IllegalArgumentException();

            if (data.remaining() <= iab.remaining())
                return iab.put(data);

            var newBuf = IntBuffer.allocate(Math.max(v.capacity() << 1, v.capacity() + data.remaining()));
            return newBuf.put(iab.flip()).put(data);
        });
    }

    @Override
    public PlutoCommandDrawMeshHeap merge(LiCommand other)
    {
        if (!(other instanceof PlutoCommandDrawMesh pcdm))
            throw new UnsupportedOperationException();

        pcdm.data.forEach((k, v) -> {
            var attrInfo = this.attributeInfo.get(k);

            switch (attrInfo.type())
            {
                case FLOAT -> this.addAttribute(k, (FloatBuffer) v, attrInfo.dimensions());
                case INT -> this.addAttribute(k, (IntBuffer) v, attrInfo.dimensions());
                case UNSIGNED_INT -> throw new UnsupportedOperationException();
            }
        });

        this.addIndices(pcdm.indices);

        return this;
    }
}
