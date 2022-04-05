package org.plutoengine.graphics.gl.vao;

import org.plutoengine.graphics.gl.vao.attrib.ReservedAttributes;
import org.plutoengine.graphics.gl.vao.attrib.data.VecArray;
import org.plutoengine.graphics.gl.vbo.FloatArrayBuffer;
import org.plutoengine.graphics.gl.vbo.IndexArrayBuffer;

public class VertexArrayBuilder
{
    protected VertexArray va;

    public VertexArrayBuilder()
    {
        this.va = new VertexArray();
    }

    public VertexArrayBuilder vertices(VecArray<float[]> vertices)
    {
        this.va.createArrayAttribute(new FloatArrayBuffer(vertices), ReservedAttributes.POSITION);

        return this;
    }

    public VertexArrayBuilder uvs(VecArray<float[]> uvs)
    {
        this.va.createArrayAttribute(new FloatArrayBuffer(uvs), ReservedAttributes.UV);

        return this;
    }

    public VertexArrayBuilder indices(VecArray<int[]> indices)
    {
        this.va.bindIndices(new IndexArrayBuffer(indices));

        return this;
    }

    public VertexArray export()
    {
        return this.va;
    }
}
