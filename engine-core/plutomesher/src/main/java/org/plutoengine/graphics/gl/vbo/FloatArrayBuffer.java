package org.plutoengine.graphics.gl.vbo;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public final class FloatArrayBuffer extends ArrayBuffer<FloatBuffer>
{
    private FloatArrayBuffer(int size)
    {
        super(GL33.GL_ARRAY_BUFFER, size);
    }

    @Override
    public void writePartialData(FloatBuffer data, long offset)
    {
        GL33.glBufferSubData(this.type, offset, data);
    }

    @Override
    public EnumArrayBufferType getDataType()
    {
        return EnumArrayBufferType.FLOAT;
    }

    public static FloatArrayBuffer from(FloatBuffer data)
    {
        if (!data.isDirect())
        {
            var fb = MemoryUtil.memAllocFloat(data.remaining());
            fb.put(data);
            fb.flip();
            var fab = from(fb);
            MemoryUtil.memFree(fb);
            return fab;
        }

        var fab = new FloatArrayBuffer(data.remaining());
        fab.bind();
        GL33.glBufferData(fab.type, data, GL33.GL_STATIC_DRAW);
        return fab;
    }

    public static FloatArrayBuffer from(float[] data)
    {
        var fab = new FloatArrayBuffer(data.length);
        fab.bind();
        GL33.glBufferData(fab.type, data, GL33.GL_STATIC_DRAW);
        return fab;
    }

    public static FloatArrayBuffer empty(int size)
    {
        var fab = new FloatArrayBuffer(size);
        fab.bind();
        GL33.glBufferData(fab.type, size, GL33.GL_STATIC_DRAW);
        return fab;
    }
}
