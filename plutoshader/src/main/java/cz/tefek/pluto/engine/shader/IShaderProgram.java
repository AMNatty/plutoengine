package cz.tefek.pluto.engine.shader;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.shader.type.IShader;

public interface IShaderProgram
{
    int getID();

    default void attach(IShader shader)
    {
        GL33.glAttachShader(this.getID(), shader.getID());
    }

    default void detach(IShader shader)
    {
        GL33.glDetachShader(this.getID(), shader.getID());
    }

    default void start()
    {
        GL33.glUseProgram(this.getID());
    }

    default void stop()
    {
        GL33.glUseProgram(0);
    }

    void dispose();
}
