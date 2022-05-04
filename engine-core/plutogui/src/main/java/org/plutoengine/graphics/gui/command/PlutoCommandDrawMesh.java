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
import org.plutoengine.libra.command.impl.LiCommand;
import org.plutoengine.libra.command.impl.LiCommandDrawMesh;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public abstract sealed class PlutoCommandDrawMesh extends LiCommandDrawMesh permits PlutoCommandDrawMeshDirectBuffer, PlutoCommandDrawMeshHeap
{
    protected final Map<Integer, AttributeInfo> attributeInfo;
    protected final Map<Integer, Buffer> data;
    protected IntBuffer indices;

    public PlutoCommandDrawMesh()
    {
        this.attributeInfo = new TreeMap<>();
        this.data = new TreeMap<>();
    }

    public IntBuffer getIndices()
    {
        if (this.indices == null)
            return null;

        return this.indices;
    }

    public Map<Integer, AttributeInfo> getAttributeInfo()
    {
        return Collections.unmodifiableMap(this.attributeInfo);
    }

    public Map<Integer, Buffer> getData()
    {
        return Collections.unmodifiableMap(this.data);
    }

    public void addIndices(int[] data)
    {
        if (data == null)
            return;

        this.addIndices(IntBuffer.wrap(data));
    }

    public abstract void addIndices(IntBuffer data);

    public void addAttribute(int attrib, float[] data, int dimensions)
    {
        if (data == null)
            return;

        this.addAttribute(attrib, FloatBuffer.wrap(data), dimensions);
    }

    public abstract void addAttribute(int attrib, FloatBuffer data, int dimensions);

    public void addAttribute(int attrib, int[] data, int dimensions)
    {
        if (data == null)
            return;

        this.addAttribute(attrib, IntBuffer.wrap(data), dimensions);
    }

    public abstract void addAttribute(int attrib, IntBuffer data, int dimensions);

    @Override
    public boolean supportsMerge(LiCommand other)
    {
        if (!(other instanceof PlutoCommandDrawMesh pcdm))
            return false;

        return this.attributeInfo.equals(pcdm.attributeInfo);
    }

    @Override
    public PlutoCommandDrawMesh merge(LiCommand other)
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

        this.addIndices(pcdm.getIndices());

        return this;
    }
}
