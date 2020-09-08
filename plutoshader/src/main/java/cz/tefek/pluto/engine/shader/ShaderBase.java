package cz.tefek.pluto.engine.shader;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public abstract class ShaderBase implements IShaderProgram
{
    private int programID;

    protected int getUniform(String name)
    {
        return GL33.glGetUniformLocation(this.programID, name);
    }

    @Override
    public void dispose()
    {
        Logger.logf(SmartSeverity.REMOVED, "Disposing of shader ID %d of type %s...\n", this.getID(), this.getClass().getCanonicalName());

        this.stop();
        GL33.glDeleteProgram(this.programID);
    }

    protected void bindAttribute(int attribute, String name)
    {
        GL33.glBindAttribLocation(this.programID, attribute, name);
    }

    @Override
    public int getID()
    {
        return this.programID;
    }
}
