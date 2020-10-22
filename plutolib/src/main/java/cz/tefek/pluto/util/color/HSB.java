package cz.tefek.pluto.util.color;

import org.apache.commons.lang3.Range;

/**
 * TODO
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class HSB
{
    /**
     * Hue [0°..360°]
     * */
    protected float h;

    /**
     * Saturation [0..1]
     * */
    protected float s;
    private final static Range<Float> SATURATION_RANGE = Range.between(0.0f, 1.0f);

    /**
     * Value/Brightness [0..1]
     * */
    protected float b;
    private final static Range<Float> BRIGHTNESS_RANGE = Range.between(0.0f, 1.0f);

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSB(float hue, float saturation, float brightness)
    {
        this.h = (360.0f + hue % 360.0f) % 360.0f;
        this.s = SATURATION_RANGE.fit(saturation);
        this.b = BRIGHTNESS_RANGE.fit(brightness);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGB toRGB()
    {
        return this.toRGBA(1.0f);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGB toRGBA()
    {
        return this.toRGBA(1.0f);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGBA toRGBA(float alpha)
    {
        float h6 = this.h / 60.0f;

        int hueSide = (int) h6;

        // The color component furthest on the hue wheel
        float p = this.b * (1 - this.s);

        float hueFractCCW = h6 - hueSide;
        // The second nearest color component on the hue wheel - counter-clockwise
        float q = this.b * (1 - hueFractCCW * this.s);

        float hueFractCW = 1 - hueFractCCW;
        // The second nearest color component on the hue wheel - clockwise
        float t = this.b * (1 - hueFractCW * this.s);

        switch (hueSide % 6)
        {
            case 1: // Hues 60°-119° -- Green is the brightest color, no blue is present at max saturation
                return new RGBA(q, this.b, p, alpha);

            case 2: // Hues 120°-179° -- Green is the brightest color, no red is present at max saturation
                return new RGBA(p, this.b, t, alpha);

            case 3: // Hues 180°-239° -- Blue is the brightest color, no red is present at max saturation
                return new RGBA(p, q, this.b, alpha);

            case 4: // Hues 240°-299° -- Blue is the brightest color, no green is present at max saturation
                return new RGBA(t, p, this.b, alpha);

            case 5: // Hues 300°-359° -- Red is the brightest color, no green is present at max saturation
                return new RGBA(this.b, p, q, alpha);

            case 0: // Hues 0°-59° -- Red is the brightest color, no blue is present at max saturation
                return new RGBA(this.b, t, p, alpha);

            default:
                throw new IllegalStateException("This HSB object's hue is negative - this is not legal.");
        }
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float hue()
    {
        return this.h;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float saturation()
    {
        return this.s;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float brightness()
    {
        return this.b;
    }
}
