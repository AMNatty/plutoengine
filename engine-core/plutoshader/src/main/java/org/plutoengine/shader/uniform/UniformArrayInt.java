package org.plutoengine.shader.uniform;

import org.lwjgl.opengl.GL33;

public class UniformArrayInt extends UniformBase
{
    public UniformArrayInt(int location)
    {
        super(location);
    }

    public void load(int[] data)
    {
        GL33.glUniform1iv(this.location, data);
    }

}
