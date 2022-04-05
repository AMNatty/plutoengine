package org.plutoengine.graphics.gl.vbo;

import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.gl.vao.attrib.data.VecArray;

public class FloatArrayBuffer extends ArrayBuffer<VecArray<float[]>>
{
    public FloatArrayBuffer(VecArray<float[]> data)
    {
        super(data);
    }

    @Override
    protected void bindData(VecArray<float[]> vertexData)
    {
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertexData.getData(), GL33.GL_STATIC_DRAW);
    }

    @Override
    public EnumArrayBufferType getType()
    {
        return EnumArrayBufferType.FLOAT;
    }
}
