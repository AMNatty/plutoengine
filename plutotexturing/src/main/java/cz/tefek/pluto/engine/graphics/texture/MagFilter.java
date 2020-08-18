package cz.tefek.pluto.engine.graphics.texture;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.gl.IOpenGLEnum;

public enum MagFilter implements IOpenGLEnum
{
    NEAREST(GL33.GL_NEAREST),
    LINEAR(GL33.GL_LINEAR);

    MagFilter(int id)
    {
        this.id = id;
    }

    private int id;

    @Override
    public int getGLID()
    {
        return this.id;
    }
}
