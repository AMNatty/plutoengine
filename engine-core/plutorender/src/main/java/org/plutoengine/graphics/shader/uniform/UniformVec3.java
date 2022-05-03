package org.plutoengine.graphics.shader.uniform;

import org.joml.Vector3fc;
import org.lwjgl.opengl.GL33;

public class UniformVec3 extends UniformBase
{
    public UniformVec3(int location)
    {
        super(location);
    }

    public void load(Vector3fc value)
    {
        GL33.glUniform3f(this.location, value.x(), value.y(), value.z());
    }

    public void load(float x, float y, float z)
    {
        GL33.glUniform3f(this.location, x, y, z);
    }
}
