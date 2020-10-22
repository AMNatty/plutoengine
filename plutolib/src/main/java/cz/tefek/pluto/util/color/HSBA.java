package cz.tefek.pluto.util.color;

import org.apache.commons.lang3.Range;

/**
 * TODO
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class HSBA extends HSB
{
    protected float a;
    private final static Range<Float> ALPHA_RANGE = Range.between(0.0f, 1.0f);

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSBA(float hue, float saturation, float lightness, float alpha)
    {
        super(hue, saturation, lightness);

        this.a = ALPHA_RANGE.fit(alpha);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public float alpha()
    {
        return this.a;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    @Override
    public RGB toRGBA()
    {
        return this.toRGBA(this.a);
    }
}
