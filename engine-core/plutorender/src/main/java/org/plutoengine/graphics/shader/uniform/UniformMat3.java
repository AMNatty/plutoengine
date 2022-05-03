package org.plutoengine.graphics.shader.uniform;

import org.joml.Matrix3fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class UniformMat3 extends UniformBase
{
    public UniformMat3(int location)
    {
        super(location);
    }

    public void load(Matrix3fc matrix)
    {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer buf = stack.mallocFloat(3 * 3);
            GL33.glUniformMatrix3fv(this.location, false, matrix.get(buf));
        }
    }
}
