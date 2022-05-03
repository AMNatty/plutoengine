package org.plutoengine.graphics.shader.uniform;

import org.joml.Vector2fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public class UniformArrayVec3 extends UniformBase
{
    public UniformArrayVec3(int location)
    {
        super(location);
    }

    public void load(Vector2fc... values)
    {
        final int dimensions = 3;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            var buf = stack.mallocFloat(dimensions * values.length);

            int i;
            for (i = 0; i < values.length; i++)
                values[i].get(i * dimensions, buf);

            buf.position(i * dimensions);

            buf.flip();

            GL33.glUniform3fv(this.location, buf);
        }
    }
}
