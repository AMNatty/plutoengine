package org.plutoengine.graphics.gl.vbo;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

import org.plutoengine.graphics.gl.vao.attrib.data.VecArray;

public class IndexArrayBuffer extends ArrayBuffer<VecArray<int[]>>
{
    public IndexArrayBuffer(int[] data)
    {
        super(new VecArray<>(data, 1));
    }

    public IndexArrayBuffer(VecArray<int[]> data)
    {
        super(data);

        if (data.getVecDimensions() != 1)
        {
            throw new IllegalArgumentException("Index buffers must have exactly one vertex dimension!");
        }
    }

    @Override
    public void bind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.glID);
    }

    @Override
    protected void bindData(VecArray<int[]> data)
    {
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.getData(), GL_STATIC_DRAW);
    }

    @Override
    public void unbind()
    {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public EnumArrayBufferType getType()
    {
        return EnumArrayBufferType.UNSIGNED_INT;
    }
}
