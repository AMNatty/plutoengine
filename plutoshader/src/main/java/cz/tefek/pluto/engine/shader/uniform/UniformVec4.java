package cz.tefek.pluto.engine.shader.uniform;

import org.joml.Vector4fc;
import org.lwjgl.opengl.GL33;

public class UniformVec4 extends UniformBase
{
    public UniformVec4(int location)
    {
        super(location);
    }

    public void load(Vector4fc value)
    {
        GL33.glUniform4f(this.location, value.x(), value.y(), value.z(), value.w());
    }

    public void load(float x, float y, float z, float w)
    {
        GL33.glUniform4f(this.location, x, y, z, w);
    }
}
