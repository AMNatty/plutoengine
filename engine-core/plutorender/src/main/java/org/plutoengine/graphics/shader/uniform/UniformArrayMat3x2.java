package org.plutoengine.graphics.shader.uniform;

import org.joml.Matrix3x2fc;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class UniformArrayMat3x2 extends UniformVec2
{
    public UniformArrayMat3x2(int programID)
    {
        super(programID);
    }

    public void load(Matrix3x2fc... matrices)
    {
        final int dimensions = 3 * 2;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer buf = stack.mallocFloat(dimensions * matrices.length);

            for (int i = 0; i < matrices.length; i++)
            {
                buf.position(i * dimensions);
                matrices[i].get(buf);
            }

            buf.flip();

            GL33.glUniformMatrix3x2fv(this.location, false, buf);
        }
    }
}
