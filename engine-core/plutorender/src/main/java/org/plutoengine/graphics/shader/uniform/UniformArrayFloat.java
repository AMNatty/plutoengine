package org.plutoengine.graphics.shader.uniform;

import org.lwjgl.opengl.GL33;

public class UniformArrayFloat extends UniformBase
{
    public UniformArrayFloat(int location)
    {
        super(location);
    }

    public void load(float[] data)
    {
        GL33.glUniform1fv(this.location, data);
    }
}
