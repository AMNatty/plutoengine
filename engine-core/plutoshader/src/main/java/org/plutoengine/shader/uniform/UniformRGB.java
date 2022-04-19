package org.plutoengine.shader.uniform;

import org.lwjgl.opengl.GL33;

import org.plutoengine.util.color.IRGB;

/**
 * A uniform allowing loading RGBA color data into shader uniforms.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class UniformRGB extends UniformVec3
{
    /**
     * Creates a new instance of the {@link UniformRGB} uniform
     * with the specified shader location.
     *
     * @param location The location within the shader
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public UniformRGB(int location)
    {
        super(location);
    }

    /**
     /**
     * Loads the {@link IRGB} color components into the shader uniform.
     *
     * @param value The {@link IRGB} color object
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(IRGB value)
    {
        GL33.glUniform3f(this.location, value.red(), value.green(), value.blue());
    }

    /**
     * Loads the RGB color components into the shader uniform.
     *
     * @param r The red color component, in range [0..1]
     * @param g The green color component, in range [0..1]
     * @param b The blue color component, in range [0..1]
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(float r, float g, float b)
    {
        GL33.glUniform3f(this.location, r, g, b);
    }
}
