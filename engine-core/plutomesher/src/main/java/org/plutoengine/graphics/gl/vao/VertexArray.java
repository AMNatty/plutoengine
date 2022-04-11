package org.plutoengine.graphics.gl.vao;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.graphics.gl.DrawMode;
import org.plutoengine.graphics.gl.vao.attrib.AttributeInfo;
import org.plutoengine.graphics.gl.vbo.ArrayBuffer;
import org.plutoengine.graphics.gl.vbo.IndexArrayBuffer;

import java.util.*;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public class VertexArray implements AutoCloseable
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

        Logger.logf(SmartSeverity.ADDED, "Vertex array ID %d created...%n", this.glID);
    }

    public void createArrayAttribute(AttributeInfo info, ArrayBuffer<?> buffer)
    {
        var attribID = info.position();
        var dimensions = info.dimensions();
        var type = buffer.getDataType();

        this.bind();
        buffer.bind();
        GL33.glVertexAttribPointer(attribID, dimensions, type.getGLID(), false, 0, 0);

        this.vertexAttributes.set(attribID, buffer);
        this.usedAttributes.add(attribID);

        if (!this.hasIndices())
            this.vertexCount = buffer.getSize() / dimensions;
    }

    public Set<Integer> getUsedAttributes()
    {
        return Set.copyOf(this.usedAttributes);
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
        this.usedAttributes.forEach(VertexArray::enableAttribute);
    }

    public void bindIndices(IndexArrayBuffer buffer)
    {
        this.bind();
        buffer.bind();
        this.indices = buffer;
        this.vertexCount = buffer.getSize();
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
            GL33.glDrawElements(mode.getGLID(), this.vertexCount, this.indices.getDataType().getGLID(), MemoryUtil.NULL);
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
            GL33.glDrawElementsInstanced(mode.getGLID(), this.vertexCount, this.indices.getDataType().getGLID(), MemoryUtil.NULL, count);
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

    public void close()
    {
        this.usedAttributes.stream()
                           .map(this.vertexAttributes::get)
                           .forEach(ArrayBuffer::close);
        this.vertexAttributes.clear();
        this.usedAttributes.clear();

        if (this.indices != null)
        {
            this.indices.close();
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

    public static void enableAttribute(int attribute)
    {
        GL33.glEnableVertexAttribArray(attribute);
    }

    public static void disableAttribute(int attribute)
    {
        GL33.glDisableVertexAttribArray(attribute);
    }
}
