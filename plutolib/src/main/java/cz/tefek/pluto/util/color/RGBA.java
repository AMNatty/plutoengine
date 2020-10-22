package cz.tefek.pluto.util.color;

/**
 * TODO
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public class RGBA extends RGB implements IRGBA
{
    protected float a;

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGBA(float r, float g, float b, float a)
    {
        super(r, g, b);
        this.a = a;
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
    public HSBA toHSBA()
    {
        return this.toHSBA(this.a);
    }
}
