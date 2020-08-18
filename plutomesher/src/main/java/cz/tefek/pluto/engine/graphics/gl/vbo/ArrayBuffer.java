package cz.tefek.pluto.engine.graphics.gl.vbo;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import cz.tefek.pluto.engine.graphics.gl.vao.attrib.data.VecArray;

public abstract class ArrayBuffer<T extends VecArray<?>>
{
    protected int glID = 0;

    private final int vertexDimensions;
    private final int vertexCount;

    public ArrayBuffer(T data)
    {
        this.glID = glGenBuffers();
        this.bind();
        this.bindData(data);

        this.vertexDimensions = data.getVecDimensions();
        this.vertexCount = data.getVertexCount();
    }

    public abstract EnumArrayBufferType getType();

    protected abstract void bindData(T data);

    public void bind()
    {
        glBindBuffer(GL_ARRAY_BUFFER, this.glID);
    }

    public void unbind()
    {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void delete()
    {
        glDeleteBuffers(this.glID);

        this.glID = 0;
    }

    public int getID()
    {
        return this.glID;
    }

    public int getVertexDimensions()
    {
        return this.vertexDimensions;
    }

    public int getVertexCount()
    {
        return this.vertexCount;
    }
}
