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

package org.plutoengine.graphics.vao;

import org.plutoengine.graphics.vao.attrib.AttributeInfo;
import org.plutoengine.graphics.vao.attrib.ReservedAttributes;
import org.plutoengine.graphics.vbo.EnumArrayBufferType;
import org.plutoengine.graphics.vbo.FloatArrayBuffer;
import org.plutoengine.graphics.vbo.IndexArrayBuffer;
import org.plutoengine.graphics.vbo.IntArrayBuffer;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VertexArrayBuilder
{
    protected VertexArray va;

    public VertexArrayBuilder()
    {
        this.va = new VertexArray();
    }

    public VertexArrayBuilder vertices(FloatBuffer vertices, int dimensions)
    {
        return this.attrib(ReservedAttributes.POSITION, dimensions, vertices);
    }

    public VertexArrayBuilder uvs(FloatBuffer uvs, int dimensions)
    {
        return this.attrib(ReservedAttributes.UV, dimensions, uvs);
    }

    public VertexArrayBuilder colors(FloatBuffer colors, int dimensions)
    {
        return this.attrib(ReservedAttributes.COLOR, dimensions, colors);
    }

    public VertexArrayBuilder vertices(float[] vertices, int dimensions)
    {
        return this.vertices(FloatBuffer.wrap(vertices), dimensions);
    }

    public VertexArrayBuilder uvs(float[] uvs, int dimensions)
    {
        return this.uvs(FloatBuffer.wrap(uvs), dimensions);
    }

    public VertexArrayBuilder colors(float[] colors, int dimensions)
    {
        return this.colors(FloatBuffer.wrap(colors), dimensions);
    }

    public VertexArrayBuilder attrib(int attribute, int dimensions, Buffer data)
    {
        if (data instanceof FloatBuffer fab)
        {
            var attrInfo = new AttributeInfo(EnumArrayBufferType.FLOAT, attribute, dimensions);
            var attr = FloatArrayBuffer.from(fab);
            this.va.createArrayAttribute(attrInfo, attr);
        }
        else if (data instanceof IntBuffer iab)
        {
            var attrInfo = new AttributeInfo(EnumArrayBufferType.INT, attribute, dimensions);
            var attr = IntArrayBuffer.from(iab);
            this.va.createArrayAttribute(attrInfo, attr);
        }

        return this;
    }

    public VertexArrayBuilder indices(int[] indices)
    {
        return this.indices(IntBuffer.wrap(indices));
    }

    public VertexArrayBuilder indices(IntBuffer indices)
    {
        var data = IndexArrayBuffer.from(indices);
        this.va.bindIndices(data);

        return this;
    }

    public VertexArray build()
    {
        return this.va;
    }
}
