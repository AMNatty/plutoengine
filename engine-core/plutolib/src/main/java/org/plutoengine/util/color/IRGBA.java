package org.plutoengine.util.color;

/**
 * An interface for single precision RGBA color objects.
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public interface IRGBA extends IRGB
{
    /**
     * Returns the alpha color component.
     *
     * @return The alpha component, in range 0..1
     */
    float alpha();
}
