package cz.tefek.pluto.engine.graphics.gl.vao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import cz.tefek.pluto.engine.graphics.gl.DrawMode;
import cz.tefek.pluto.engine.graphics.gl.vbo.ArrayBuffer;
import cz.tefek.pluto.engine.graphics.gl.vbo.IndexArrayBuffer;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public class VertexArray
{
    protected final List<Integer> usedAttribs;
    protected final Vector<ArrayBuffer<?>> vertexAttribs;

    protected IndexArrayBuffer indices;

    private int vertexCount;
    protected int glID = 0;

    public VertexArray()
    {
        int maxAttribs = GL33.glGetInteger(GL33.GL_MAX_VERTEX_ATTRIBS);

        this.usedAttribs = new ArrayList<>(maxAttribs);
        this.vertexAttribs = new Vector<ArrayBuffer<?>>(maxAttribs);
        this.vertexAttribs.setSize(maxAttribs);

        this.glID = GL33.glGenVertexArrays();

        Logger.logf(SmartSeverity.ADDED, "Vertex array ID %d created...\n", this.glID);
    }

    public void createArrayAttrib(ArrayBuffer<?> buffer, int attribID)
    {
        this.bind();
        buffer.bind();
        GL33.glVertexAttribPointer(attribID, buffer.getVertexDimensions(), buffer.getType().getGLID(), false, 0, 0);

        this.vertexAttribs.set(attribID, buffer);
        this.usedAttribs.add(attribID);

        if (!this.hasIndices())
        {
            this.vertexCount = buffer.getVertexCount();
        }
    }

    public List<ArrayBuffer<?>> getVertexAttribs()
    {
        return Collections.unmodifiableList(this.vertexAttribs);
    }

    public int getVertexCount()
    {
        return this.vertexCount;
    }

    public void enableAllAttributes()
    {
        this.usedAttribs.stream().forEach(GL33::glEnableVertexAttribArray);
    }

    public void bindIndices(IndexArrayBuffer buffer)
    {
        this.bind();
        buffer.bind();
        this.indices = buffer;
        this.vertexCount = buffer.getVertexCount();
    }

    public void bind()
    {
        GL33.glBindVertexArray(this.glID);
    }

    public void unbind()
    {
        GL33.glBindVertexArray(0);
    }

    public void draw(DrawMode mode)
    {
        if (this.hasIndices())
        {
            GL33.glDrawElements(mode.getGLID(), this.vertexCount, this.indices.getType().getGLID(), MemoryUtil.NULL);
        }
        else
        {
            GL33.glDrawArrays(mode.getGLID(), 0, this.vertexCount);
        }
    }

    public void drawInstanced(DrawMode mode, int count)
    {
        if (this.hasIndices())
        {
            GL33.glDrawElementsInstanced(mode.getGLID(), this.vertexCount, this.indices.getType().getGLID(), MemoryUtil.NULL, count);
        }
        else
        {
            GL33.glDrawArraysInstanced(mode.getGLID(), 0, this.vertexCount, count);
        }
    }

    public IndexArrayBuffer getIndices()
    {
        return this.indices;
    }

    public boolean hasIndices()
    {
        return this.indices != null;
    }

    public void delete()
    {
        this.usedAttribs.stream().map(this.vertexAttribs::get).forEach(ArrayBuffer::delete);
        this.vertexAttribs.clear();
        this.usedAttribs.clear();

        if (this.indices != null)
        {
            this.indices.delete();
            this.indices = null;
        }

        Logger.logf(SmartSeverity.REMOVED, "Vertex array ID %d deleted...\n", this.glID);

        GL33.glDeleteVertexArrays(this.glID);

        this.glID = 0;
    }

    public int getID()
    {
        return this.glID;
    }
}
