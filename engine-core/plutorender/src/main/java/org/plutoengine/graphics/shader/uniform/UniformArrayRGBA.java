package org.plutoengine.graphics.shader.uniform;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.plutoengine.util.color.IRGBA;

/**
 * A uniform allowing loading RGBA color arrays into shader uniforms.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class UniformArrayRGBA extends UniformVec4
{
    /**
     * Creates a new instance of the {@link UniformArrayRGBA} uniform
     * with the specified shader location.
     *
     * @param location The location within the shader
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public UniformArrayRGBA(int location)
    {
        super(location);
    }

    /**
     * Loads the {@link IRGBA} color components into the shader uniform.
     *
     * @param values The {@link IRGBA} color object array
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(IRGBA[] values)
    {
        final int dimensions = 4;

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            var buf = stack.mallocFloat(dimensions * values.length);

            for (var value : values)
            {
                buf.put(value.red())
                   .put(value.green())
                   .put(value.blue())
                   .put(value.alpha());
            }

            buf.flip();

            GL33.glUniform4fv(this.location, buf);
        }
    }
}
