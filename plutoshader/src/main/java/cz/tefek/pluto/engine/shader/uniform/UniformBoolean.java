package cz.tefek.pluto.engine.shader.uniform;

import org.lwjgl.opengl.GL33;

public class UniformBoolean extends UniformBase
{
    public UniformBoolean(int location)
    {
        super(location);
    }

    public void load(boolean value)
    {
        GL33.glUniform1i(this.location, value ? 1 : 0);
    }
}
