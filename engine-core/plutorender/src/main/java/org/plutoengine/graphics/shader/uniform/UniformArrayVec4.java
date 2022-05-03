package org.plutoengine.graphics.shader.uniform;

import org.joml.Vector4fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

public class UniformArrayVec4 extends UniformBase
{
    public UniformArrayVec4(int location)
    {
        super(location);
    }

    public void load(Vector4fc... values)
    {
        final int dimensions = 4;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            var buf = stack.mallocFloat(dimensions * values.length);

            for (int i = 0; i < values.length; i++)
                values[i].get(i * dimensions, buf);

            buf.flip();

            GL33.glUniform4fv(this.location, buf);
        }
    }
}
