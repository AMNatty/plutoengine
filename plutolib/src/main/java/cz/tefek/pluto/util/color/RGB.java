package cz.tefek.pluto.util.color;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * TODO
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class RGB implements IRGB
{
    protected float r;
    protected float g;
    protected float b;

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGB(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSBA toHSB()
    {
        return this.toHSBA(1.0f);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSBA toHSBA()
    {
        return this.toHSBA(1.0f);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSBA toHSBA(float alpha)
    {
        float brightness = NumberUtils.max(this.r, this.g, this.b);
        float min = NumberUtils.min(this.r, this.g, this.b);

        if (brightness == 0)
            return new HSBA(0, 0, 0, alpha);

        float chroma = brightness - min;

        if (chroma == 0)
            return new HSBA(0, 0, brightness, alpha);

        float saturation = chroma / brightness;
        float hue;

        if (brightness == this.r)
            if (this.g < this.b)
                hue = (this.g - this.b) / chroma + 6;
            else
                hue = (this.g - this.b) / chroma;
        else if (brightness == this.g)
            hue = (this.b - this.r) / chroma + 2;
        else
            hue = (this.r - this.g) / chroma + 4;

        hue *= 60;

        return new HSBA(hue, saturation, brightness, alpha);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float red()
    {
        return this.r;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float green()
    {
        return this.g;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float blue()
    {
        return this.b;
    }
}
