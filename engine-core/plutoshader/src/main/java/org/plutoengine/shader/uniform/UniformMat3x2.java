package org.plutoengine.shader.uniform;

import org.joml.Matrix3x2fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class UniformMat3x2 extends UniformVec2
{
    public UniformMat3x2(int programID)
    {
        super(programID);
    }

    public void load(Matrix3x2fc matrix)
    {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer buf = stack.mallocFloat(3 * 2);
            GL33.glUniformMatrix3x2fv(this.location, false, matrix.get(buf));
        }
    }
}
