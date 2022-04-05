package org.plutoengine.graphics.gl.vao;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.graphics.gl.DrawMode;
import org.plutoengine.graphics.gl.vbo.ArrayBuffer;
import org.plutoengine.graphics.gl.vbo.IndexArrayBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public class VertexArray
{
    protected final List<Integer> usedAttributes;
    protected final Vector<ArrayBuffer<?>> vertexAttributes;

    protected IndexArrayBuffer indices;

    private int vertexCount;
    protected int glID;

    public VertexArray()
    {
        int maxAttributes = GL33.glGetInteger(GL33.GL_MAX_VERTEX_ATTRIBS);

        this.usedAttributes = new ArrayList<>(maxAttributes);
        this.vertexAttributes = new Vector<>(maxAttributes);
        this.vertexAttributes.setSize(maxAttributes);

        this.glID = GL33.glGenVertexArrays();

        Logger.logf(SmartSeverity.ADDED, "Vertex array ID %d created...\n", this.glID);
    }

    public void createArrayAttribute(ArrayBuffer<?> buffer, int attribID)
    {
        this.bind();
        buffer.bind();
        GL33.glVertexAttribPointer(attribID, buffer.getVertexDimensions(), buffer.getType().getGLID(), false, 0, 0);

        this.vertexAttributes.set(attribID, buffer);
        this.usedAttributes.add(attribID);

        if (!this.hasIndices())
        {
            this.vertexCount = buffer.getVertexCount();
        }
    }

    public List<ArrayBuffer<?>> getVertexAttributes()
    {
        return Collections.unmodifiableList(this.vertexAttributes);
    }

    public int getVertexCount()
    {
        return this.vertexCount;
    }

    public void enableAllAttributes()
    {
        this.usedAttributes.forEach(GL33::glEnableVertexAttribArray);
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
        this.usedAttributes.stream().map(this.vertexAttributes::get).forEach(ArrayBuffer::delete);
        this.vertexAttributes.clear();
        this.usedAttributes.clear();

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
