package cz.tefek.pluto.engine.shader.ubo;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.shader.ShaderBase;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public final class UniformBufferObject
{
    private int id;
    private long size;
    private String name;

    public UniformBufferObject(long size, String name)
    {
        this.id = GL33.glGenBuffers();
        this.size = size;
        this.name = name;

        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, this.id);
        GL33.glBufferData(GL33.GL_UNIFORM_BUFFER, this.size, GL33.GL_DYNAMIC_DRAW);
        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, 0);

        GL33.glBindBufferRange(GL33.GL_UNIFORM_BUFFER, 0, this.id, 0, this.size);
    }

    public void bindLocation(ShaderBase shader, int bindingPoint)
    {
        var programID = shader.getID();

        int id = GL33.glGetUniformBlockIndex(programID, this.name);

        if (id == GL33.GL_INVALID_INDEX)
        {
            Logger.logf(SmartSeverity.ERROR, "Uniform block %s not found in shader %d.\n", this.name, programID);
            GL33.glUseProgram(0);
            return;
        }

        GL33.glUniformBlockBinding(programID, id, bindingPoint);
    }

    public void writeIntData(long offset, int[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void writeLongData(long offset, long[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void writeFloatData(long offset, float[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void writeDoubleData(long offset, double[] data)
    {
        this.bind();
        GL33.glBufferSubData(GL33.GL_UNIFORM_BUFFER, offset, data);
        this.unbind();
    }

    public void bind()
    {
        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, this.id);
    }

    public void unbind()
    {
        GL33.glBindBuffer(GL33.GL_UNIFORM_BUFFER, 0);
    }

    public void free()
    {
        GL33.glDeleteBuffers(this.id);
    }
}
