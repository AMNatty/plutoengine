package cz.tefek.pluto.engine.graphics.texture;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.gl.IOpenGLEnum;

public enum MinFilter implements IOpenGLEnum
{
    NEAREST(GL33.GL_NEAREST, false),
    LINEAR(GL33.GL_LINEAR, false),
    NEAREST_MIPMAP_NEAREST(GL33.GL_NEAREST_MIPMAP_NEAREST, true),
    LINEAR_MIPMAP_NEAREST(GL33.GL_LINEAR_MIPMAP_NEAREST, true),
    NEAREST_MIPMAP_LINEAR(GL33.GL_NEAREST_MIPMAP_LINEAR, true),
    LINEAR_MIPMAP_LINEAR(GL33.GL_LINEAR_MIPMAP_LINEAR, true);

    MinFilter(int id, boolean isMipMapped)
    {
        this.id = id;
        this.mipMapped = isMipMapped;
    }

    private int id;
    private boolean mipMapped;

    @Override
    public int getGLID()
    {
        return this.id;
    }

    public boolean isMipMapped()
    {
        return this.mipMapped;
    }
}
