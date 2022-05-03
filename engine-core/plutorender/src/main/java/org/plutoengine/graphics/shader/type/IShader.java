package org.plutoengine.graphics.shader.type;

import org.lwjgl.opengl.GL33;

public interface IShader extends AutoCloseable
{
    int getID();

    default void close()
    {
        GL33.glDeleteShader(this.getID());
    }
}
