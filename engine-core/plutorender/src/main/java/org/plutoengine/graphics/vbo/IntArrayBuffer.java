package org.plutoengine.graphics.vbo;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public non-sealed class IntArrayBuffer extends ArrayBuffer<IntBuffer>
{
    private IntArrayBuffer(int size)
    {
        super(GL33.GL_ARRAY_BUFFER, size);
    }

    @Override
    public void writePartialData(IntBuffer data, long offset)
    {
        GL33.glBufferSubData(this.type, offset, data);
    }

    @Override
    public EnumArrayBufferType getDataType()
    {
        return EnumArrayBufferType.INT;
    }

    public static IntArrayBuffer from(IntBuffer data)
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

        var iab = new IntArrayBuffer(data.remaining());
        iab.bind();
        GL33.glBufferData(iab.type, data, GL33.GL_STATIC_DRAW);
        return iab;
    }

    public static IntArrayBuffer from(int[] data)
    {
        var iab = new IntArrayBuffer(data.length);
        iab.bind();
        GL33.glBufferData(iab.type, data, GL33.GL_STATIC_DRAW);
        return iab;
    }

    public static IntArrayBuffer empty(int size)
    {
        var iab = new IntArrayBuffer(size);
        iab.bind();
        GL33.glBufferData(iab.type, size, GL33.GL_DYNAMIC_DRAW);
        return iab;
    }
}
