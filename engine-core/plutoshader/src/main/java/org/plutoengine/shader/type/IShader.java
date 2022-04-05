package org.plutoengine.shader.type;

import org.lwjgl.opengl.GL33;

public interface IShader
{
    int getID();

    default void dispose()
    {
        GL33.glDeleteShader(this.getID());
    }
}
