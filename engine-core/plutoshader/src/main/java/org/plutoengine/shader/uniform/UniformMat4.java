package org.plutoengine.shader.uniform;

import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class UniformMat4 extends UniformBase
{
    public UniformMat4(int location)
    {
        super(location);
    }

    public void load(Matrix4fc matrix)
    {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer buf = stack.mallocFloat(4 * 4);
            GL33.glUniformMatrix4fv(this.location, false, matrix.get(buf));
        }
    }
}
