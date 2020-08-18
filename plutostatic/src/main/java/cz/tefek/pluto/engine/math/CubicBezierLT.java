package cz.tefek.pluto.engine.math;

/**
 * A class to generate a cubic bezier interpolation function. This
 * implementation creates a lookup table.
 * 
 * @since 0.2
 * @author 493msi
 */
public class CubicBezierLT
{
    private static final int tableSize = 1 << 11;
    private final double[] values;
    private final double upperBound = tableSize - 1.0;

    /**
     * Creates a new {@code CubicBezierLT} from the given parameters.
     * 
     * @param cpx1 the X position of the direction the function "steers towards"
     * @param cpy1 the Y position of the direction the function "steers towards"
     * @param cpx2 the X position of the direction the function "arrives from"
     * @param cpy2 the Y position of the direction the function "arrives from"
     * 
     * @since 0.2
     * @author 493msi
     */
    public CubicBezierLT(double cpx1, double cpy1, double cpx2, double cpy2)
    {
        if (cpx1 < 0 || cpx1 > 1 || cpx2 < 0 || cpx2 > 1)
        {
            throw new IllegalArgumentException("Parameter out of range, only 0..1 is supported (only one Y value in 0..1 for each X in 0..1).");
        }

        var cubicBezier = new CubicBezier(cpx1, cpy1, cpx2, cpy2);

        this.values = new double[tableSize];

        for (int i = 0; i < tableSize; i++)
        {
            values[i] = cubicBezier.forX(i / upperBound);
        }
    }

    /**
     * Retrives the approximate value for the given x and the supplied
     * parameters in the constructor.
     * 
     * @param xIn the input X position
     * 
     * @return the approximate Y position for the given X position
     * 
     * @since 0.2
     * @author 493msi
     */
    public double forX(double xIn)
    {
        if (xIn < 0)
            return forX(0);

        if (xIn > 1)
            return forX(1);

        return values[(int) (xIn * upperBound)];
    }
}
