package org.plutoengine.graphics.gl.vbo;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public final class IndexArrayBuffer extends ArrayBuffer<IntBuffer>
{
    private IndexArrayBuffer(int size)
    {
        super(GL33.GL_ELEMENT_ARRAY_BUFFER, size);
    }

    @Override
    public void writePartialData(IntBuffer data, long offset)
    {
        GL33.glBufferSubData(this.type, offset, data);
    }

    @Override
    public EnumArrayBufferType getDataType()
    {
        return EnumArrayBufferType.UNSIGNED_INT;
    }

    public static IndexArrayBuffer from(IntBuffer data)
    {
        if (!data.isDirect())
        {
            var ib = MemoryUtil.memAllocInt(data.remaining());
            ib.put(data);
            ib.flip();
            var iab = from(ib);
            MemoryUtil.memFree(ib);
            return iab;
        }

        var iab = new IndexArrayBuffer(data.remaining());
        iab.bind();
        GL33.glBufferData(iab.type, data, GL33.GL_STATIC_DRAW);
        return iab;
    }

    public static IndexArrayBuffer from(int[] data)
    {
        var iab = new IndexArrayBuffer(data.length);
        iab.bind();
        GL33.glBufferData(iab.type, data, GL33.GL_STATIC_DRAW);
        return iab;
    }

    public static IndexArrayBuffer empty(int size)
    {
        var iab = new IndexArrayBuffer(size);
        iab.bind();
        GL33.glBufferData(iab.type, size, GL33.GL_DYNAMIC_DRAW);
        return iab;
    }
}
