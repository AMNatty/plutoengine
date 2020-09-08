package cz.tefek.pluto.engine.graphics.gl.vao;

import cz.tefek.pluto.engine.graphics.gl.vao.attrib.ReservedAttributes;
import cz.tefek.pluto.engine.graphics.gl.vao.attrib.data.VecArray;
import cz.tefek.pluto.engine.graphics.gl.vbo.FloatArrayBuffer;
import cz.tefek.pluto.engine.graphics.gl.vbo.IndexArrayBuffer;

public class VertexArrayBuilder
{
    protected VertexArray va;

    public VertexArrayBuilder()
    {
        this.va = new VertexArray();
    }

    public VertexArrayBuilder vertices(VecArray<float[]> vertices)
    {
        this.va.createArrayAttrib(new FloatArrayBuffer(vertices), ReservedAttributes.POSITION);

        return this;
    }

    public VertexArrayBuilder uvs(VecArray<float[]> uvs)
    {
        this.va.createArrayAttrib(new FloatArrayBuffer(uvs), ReservedAttributes.UV);

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
