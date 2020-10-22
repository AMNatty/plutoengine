package cz.tefek.pluto.engine.shader.uniform;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.util.color.IRGBA;

/**
 * A uniform allowing loading RGBA color data into shader uniforms.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class UniformRGBA extends UniformBase
{
    /**
     * Creates a new instance of the {@link UniformRGBA} uniform
     * with the specified shader location.
     *
     * @param location The location within the shader
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public UniformRGBA(int location)
    {
        super(location);
    }

    /**
     * Loads the {@link IRGBA} color components into the shader uniform.
     *
     * @param value The {@link IRGBA} color object
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(IRGBA value)
    {
        GL33.glUniform4f(this.location, value.red(), value.green(), value.blue(), value.alpha());
    }

    /**
     * Loads the RGBA color components into the shader uniform.
     *
     * @param r The red color component, in range [0..1]
     * @param g The green color component, in range [0..1]
     * @param b The blue color component, in range [0..1]
     * @param a The alpha component, in range [0..1]
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void load(float r, float g, float b, float a)
    {
        GL33.glUniform4f(this.location, r, g, b, a);
    }
}
