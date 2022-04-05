package org.plutoengine.util.color;

/**
 * An interface for single precision RGB color objects.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public interface IRGB
{
    /**
     * Returns the red color component.
     *
     * @return The red component, in range 0..1
     */
    float red();

    /**
     * Returns the green color component.
     *
     * @return The green component, in range 0..1
     */
    float green();

    /**
     * Returns the blue color component.
     *
     * @return The blue color component, in range 0..1
     */
    float blue();
}
