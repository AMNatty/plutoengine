package org.plutoengine.graphics.gl.vbo;

import org.lwjgl.opengl.GL33;

import java.nio.Buffer;

public sealed abstract class ArrayBuffer<T extends Buffer> implements AutoCloseable permits FloatArrayBuffer, IndexArrayBuffer, IntArrayBuffer
{
    protected int glID;

    protected final int type;
    private final int size;

    protected ArrayBuffer(int type, int size)
    {
        this.glID = GL33.glGenBuffers();
        this.type = type;
        this.size = size;
    }

    public abstract EnumArrayBufferType getDataType();

    public abstract void writePartialData(T data, long offset);

    public int getSize()
    {
        return this.size;
    }

    public void bind()
    {
        GL33.glBindBuffer(this.type, this.glID);
    }

    public void unbind()
    {
        GL33.glBindBuffer(this.type, 0);
    }

    public void close()
    {
        GL33.glDeleteBuffers(this.glID);

        this.glID = 0;
    }

    public int getID()
    {
        return this.glID;
    }
}
