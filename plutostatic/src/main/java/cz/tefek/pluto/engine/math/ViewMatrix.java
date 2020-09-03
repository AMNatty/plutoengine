package cz.tefek.pluto.engine.math;

import org.joml.Matrix3x2f;

/**
 * A class with factory methods for the most used view transformations.
 * 
 * @author 493msi
 * @since 0.3
 */
public class ViewMatrix
{
    /**
     * Create a 2D view matrix.
     * 
     * @param x The X camera translation
     * @param y The Y camera translation
     * @param zoom The zoom
     * 
     * @return the view matrix
     * 
     * @author 493msi
     * @since 0.3
     */
    public static Matrix3x2f createView(int x, int y, float zoom)
    {
        var view = new Matrix3x2f();
        view.scale(zoom);
        view.translate(x, y);

        return view;
    }
}
