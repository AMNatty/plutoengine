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
