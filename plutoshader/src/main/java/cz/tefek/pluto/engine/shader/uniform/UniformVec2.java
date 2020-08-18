package cz.tefek.pluto.engine.shader.uniform;

import org.joml.Vector2fc;
import org.lwjgl.opengl.GL33;

public class UniformVec2 extends UniformBase
{
    public UniformVec2(int location)
    {
        super(location);
    }

    public void load(Vector2fc value)
    {
        GL33.glUniform2f(this.location, value.x(), value.y());
    }

    public void load(float x, float y)
    {
        GL33.glUniform2f(this.location, x, y);
    }
}
