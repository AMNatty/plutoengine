package org.plutoengine.shader.uniform;

import org.lwjgl.opengl.GL33;

public class UniformFloat extends UniformBase
{
    public UniformFloat(int location)
    {
        super(location);
    }

    public void load(float value)
    {
        GL33.glUniform1f(this.location, value);
    }
}
