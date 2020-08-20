package cz.tefek.pluto.math;

/**
 * A class to generate a cubic bezier interpolation function. Not very
 * optimized, so use it sparsely.
 * 
 * @since 0.2
 * @author 493msi
 */
public class CubicBezier
{
    private static final int iterations = 16;
    private double a, b, c, d;

    /**
     * Creates a new {@code CubicBezier} from the given parameters.
     * 
     * @param cpx1 the X position of the direction the function "steers towards"
     * @param cpy1 the Y position of the direction the function "steers towards"
     * @param cpx2 the X position of the direction the function "arrives from"
     * @param cpy2 the Y position of the direction the function "arrives from"
     * 
     * @since 0.3
     * @author 493msi
     */
    public CubicBezier(double cpx1, double cpy1, double cpx2, double cpy2)
    {
        if (cpx1 < 0 || cpx1 > 1 || cpx2 < 0 || cpx2 > 1)
        {
            throw new IllegalArgumentException("Parameter out of range, only 0..1 is supported (only one Y value in 0..1 for each X in 0..1).");
        }

        this.a = cpx1;
        this.b = cpy1;
        this.c = cpx2;
        this.d = cpy2;
    }

    /**
     * Solves this {@code CubicBezier} for the given x and the supplied
     * parameters in the constructor.
     * 
     * @param xIn the input X position
     * 
     * @return the approximate Y position for the given X position
     * 
     * @since 0.3
     * @author 493msi
     */
    public double forX(double xIn)
    {
        if (xIn < 0)
        {
            return this.forX(0);
        }

        if (xIn > 1)
        {
            return this.forX(1);
        }

        double t = 0.5;

        double x;
        double y = 3 * (1 - t) * (1 - t) * t * this.b + 3 * (1 - t) * t * t * this.d + t * t * t;

        double delta = 0.25;
        boolean uh;

        for (int i = 0; i < iterations; i++)
        {
            x = 3 * (1 - t) * (1 - t) * t * this.a + 3 * (1 - t) * t * t * this.c + t * t * t;
            y = 3 * (1 - t) * (1 - t) * t * this.b + 3 * (1 - t) * t * t * this.d + t * t * t;

            uh = x > xIn;

            t += uh ? -delta : delta;

            delta /= 2;
        }

        return y;
    }
}
