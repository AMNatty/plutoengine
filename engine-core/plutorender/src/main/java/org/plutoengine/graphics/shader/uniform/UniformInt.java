package org.plutoengine.graphics.shader.uniform;

import org.lwjgl.opengl.GL33;

public class UniformInt extends UniformBase
{
    public UniformInt(int location)
    {
        super(location);
    }

    public void load(int value)
    {
        GL33.glUniform1i(this.location, value);
    }
}
