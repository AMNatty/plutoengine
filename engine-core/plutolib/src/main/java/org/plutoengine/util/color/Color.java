package org.plutoengine.util.color;


import org.jetbrains.annotations.NotNull;

/**
 * A simple 8-bit RGBA color container.
 *
 * <p><em>
 *  Some methods mutate the object to avoid new object creation.
 *  These methods are prefixed with "store".
 * </em></p>
 *
 * Each of the color components is stored separately as a 32-bit integer
 * to avoid unnecessary type conversion at the cost of some memory.
 *
 * <p>
 * This however should not be a problem as <em>this class is not designed
 * for large-scale or performance-sensitive color operations</em>.
 * </p>
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public final class Color
{
    // Black and white
    public static Color WHITE = new Color(255, 255, 255);
    public static Color BLACK = new Color(0, 0, 0);

    // Shades of gray
    public static Color VERY_DARK_GRAY = new Color(40, 40, 40);
    public static Color DARK_GRAY = new Color(85, 85, 85);
    public static Color GRAY = new Color(128, 128, 128);
    public static Color SILVER = new Color(192, 192, 192);
    public static Color LIGHT_GRAY = new Color(212, 212, 212);

    // Basic colors
    public static Color RED = new Color(255, 0, 0);
    public static Color GREEN = new Color(0, 255, 0);
    public static Color BLUE = new Color(0, 0, 255);
    public static Color YELLOW = new Color(255, 255, 0);


    public static Color TRANSPARENT = new Color(0, 0, 0,  0);
    public static Color TRANSPARENT_WHITE = new Color(255, 255, 255,  0);

    public static Color AMBER = new Color(255, 190, 0);
    public static Color AMETHYST = new Color(153, 102, 204);
    public static Color APRICOT = new Color(235, 147, 115);
    public static Color AZURE = new Color(0, 57, 169);
    public static Color BROWN = new Color(150, 75, 0);
    public static Color COBALT = new Color(0, 71, 171);
    public static Color COPPER = new Color(184, 115, 51);
    public static Color CORAL_RED = new Color(255, 50, 60);
    public static Color CORNFLOWER_BLUE = new Color(112, 112, 255);
    public static Color CRIMSON = new Color(220, 26, 64);
    public static Color CYAN = new Color(0, 188, 212);
    public static Color DARK_BROWN = new Color(66, 33, 0);
    public static Color DARK_GREEN = new Color(0, 150, 0);
    public static Color LIGHT_AZURE = new Color(0, 128, 255);
    public static Color LIME = new Color(191, 255, 0);
    public static Color MAGENTA = new Color(255, 0, 255);
    public static Color MALACHITE = new Color(11, 218, 81);
    public static Color NAVY_BLUE = new Color(0, 0, 128);
    public static Color OBSIDIAN = new Color(16, 18, 29);
    public static Color ORANGE = new Color(255, 102, 0);
    public static Color ORANGE_RED = new Color(255, 55, 0);
    public static Color PEAR = new Color(209, 226, 49);
    public static Color PINK = new Color(253, 109, 134);
    public static Color PUMPKIN_ORANGE = new Color(255, 117, 0);
    public static Color SAPPHIRE = new Color(47, 81, 158);
    public static Color TEAL = new Color(0, 140, 140);
    public static Color TURQUOISE = new Color(10, 255, 141);


    public static Color PASTEL_PINK = new Color(255, 192, 203);
    public static Color PASTEL_LIME = new Color(221, 255, 192);
    public static Color PASTEL_YELLOW = new Color(252, 255, 192);
    public static Color PASTEL_CYAN = new Color(192, 255, 234);
    public static Color PASTEL_BLUE = new Color(192, 199, 255);
    public static Color PASTEL_VIOLET = new Color(192, 199, 255);

    public int red;
    public int green;
    public int blue;
    public int alpha = 255;

    /**
     * Creates a new Color object from the supplied RGBA color components.
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public Color(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Creates a new Color object from the supplied RGBA color components.
     *
     * Alpha is set to 255 by default.
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public Color(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Converts the supplied float-based {@link IRGBA} color object to a new {@link Color} object and returns it.
     *
     * Color values are rounded to the nearest integer.
     *
     * @return A new {@link Color} object
     *
     * @param colorComponents An {@link IRGBA} color object
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public static Color from(@NotNull IRGBA colorComponents)
    {
        return new Color(Math.round(colorComponents.red() * 255),
                Math.round(colorComponents.green() * 255),
                Math.round(colorComponents.blue() * 255),
                Math.round(colorComponents.alpha() * 255));
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public static Color from(@NotNull IRGB colorComponents)
    {
        return new Color(Math.round(colorComponents.red() * 255),
                Math.round(colorComponents.green() * 255),
                Math.round(colorComponents.blue() * 255));
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public static Color from(int color, @NotNull EnumColorFormat colorFormat)
    {
        return switch (colorFormat)
        {
            case CF_INT_BGR -> new Color(color & 0xff, (color >> 8) & 0xff, (color >> 16) & 0xff);
            case CF_INT_RGB -> new Color((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff);
            case CF_INT_ABGR -> new Color(color & 0xff, (color >> 8) & 0xff, (color >> 16) & 0xff, (color >> 24) & 0xff);
            case CF_INT_ARGB -> new Color((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff, (color >> 24) & 0xff);
            case CF_INT_BGRA -> new Color((color >> 8) & 0xff, (color >> 16) & 0xff, (color >> 24) & 0xff, color & 0xff);
            case CF_INT_RGBA -> new Color((color >> 24) & 0xff, (color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff);
            default -> throw new UnsupportedOperationException("Use the from(byte[], int, ColorFormat) for byte color formats!");
        };
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public static Color fromAWT(@NotNull java.awt.Color color)
    {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public static Color from(byte @NotNull [] color, @NotNull EnumColorFormat colorFormat)
    {
        return from(color, 0, colorFormat);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public static Color from(byte @NotNull [] color, int offset, @NotNull EnumColorFormat colorFormat)
    {
        return switch (colorFormat)
        {
            case CF_3BYTE_BGR -> new Color(color[offset + 2] & 0xFF, color[offset + 1] & 0xFF, color[offset] & 0xFF);
            case CF_3BYTE_RGB -> new Color(color[offset] & 0xFF, color[offset + 1] & 0xFF, color[offset + 2] & 0xFF);
            case CF_4BYTE_ABGR -> new Color(color[offset + 3] & 0xFF, color[offset + 2] & 0xFF, color[offset + 1] & 0xFF, color[offset] & 0xFF);
            case CF_4BYTE_ARGB -> new Color(color[offset + 1] & 0xFF, color[offset + 2] & 0xFF, color[offset + 3] & 0xFF, color[offset] & 0xFF);
            case CF_4BYTE_BGRA -> new Color(color[offset + 2] & 0xFF, color[offset + 1] & 0xFF, color[offset] & 0xFF, color[offset + 3] & 0xFF);
            case CF_4BYTE_RGBA -> new Color(color[offset] & 0xFF, color[offset + 1] & 0xFF, color[offset + 2] & 0xFF, color[offset + 3] & 0xFF);
            default -> throw new UnsupportedOperationException("Use the from(int, ColorFormat) for int color formats!");
        };
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int getIntRGBA()
    {
        return get(EnumColorFormat.CF_INT_RGBA);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int getIntARGB()
    {
        return get(EnumColorFormat.CF_INT_ARGB);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int get(@NotNull EnumColorFormat colorFormat)
    {
        return switch (colorFormat)
        {
            case CF_INT_BGR -> (this.blue << 16) | (this.green << 8) | this.red;
            case CF_INT_RGB -> (this.red << 16) | (this.green << 8) | this.blue;
            case CF_INT_ABGR -> (this.alpha << 24) | (this.blue << 16) | (this.green << 8) | this.red;
            case CF_INT_ARGB -> (this.alpha << 24) | (this.red << 16) | (this.green << 8) | this.blue;
            case CF_INT_BGRA -> (this.blue << 24) | (this.green << 16) | (this.red << 8) | this.alpha;
            case CF_INT_RGBA -> (this.red << 24) | (this.green << 16) | (this.blue << 8) | this.alpha;
            default -> throw new UnsupportedOperationException("Use the get(ColorFormat, byte[], int) for byte color formats!");
        };
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void get(@NotNull EnumColorFormat colorFormat, byte @NotNull [] dataOut)
    {
        get(colorFormat, dataOut, 0);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void get(@NotNull EnumColorFormat colorFormat, byte @NotNull [] dataOut, int offset)
    {
        switch (colorFormat)
        {
            case CF_3BYTE_BGR -> {
                dataOut[offset++] = (byte) this.blue;
                dataOut[offset++] = (byte) this.green;
                dataOut[offset] = (byte) this.red;
            }
            case CF_3BYTE_RGB -> {
                dataOut[offset++] = (byte) this.red;
                dataOut[offset++] = (byte) this.green;
                dataOut[offset] = (byte) this.blue;
            }
            case CF_4BYTE_ABGR -> {
                dataOut[offset++] = (byte) this.alpha;
                dataOut[offset++] = (byte) this.blue;
                dataOut[offset++] = (byte) this.green;
                dataOut[offset] = (byte) this.red;
            }
            case CF_4BYTE_ARGB -> {
                dataOut[offset++] = (byte) this.alpha;
                dataOut[offset++] = (byte) this.red;
                dataOut[offset++] = (byte) this.green;
                dataOut[offset] = (byte) this.blue;
            }
            case CF_4BYTE_BGRA -> {
                dataOut[offset++] = (byte) this.blue;
                dataOut[offset++] = (byte) this.green;
                dataOut[offset++] = (byte) this.red;
                dataOut[offset] = (byte) this.alpha;
            }
            case CF_4BYTE_RGBA -> {
                dataOut[offset++] = (byte) this.red;
                dataOut[offset++] = (byte) this.green;
                dataOut[offset++] = (byte) this.blue;
                dataOut[offset] = (byte) this.alpha;
            }
            default -> throw new UnsupportedOperationException("Use the get(ColorFormat) for int color formats!");
        }
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int getRed()
    {
        return this.red;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int getGreen()
    {
        return this.green;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int getBlue()
    {
        return this.blue;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public int getAlpha()
    {
        return this.alpha;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGB getFloatComponentsRGB()
    {
        return new RGB(this.red / 255.0f, this.green / 255.0f, this.blue / 255.0f);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public RGBA getFloatComponentsRGBA()
    {
        return new RGBA(this.red / 255.0f, this.green / 255.0f, this.blue / 255.0f, this.alpha / 255.0f);
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSB getFloatComponentsHSB()
    {
        return this.getFloatComponentsRGB().toHSB();
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public HSBA getFloatComponentsHSBA()
    {
        return this.getFloatComponentsRGBA().toHSBA();
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void storeFloatComponentsRGBA(@NotNull RGBA target)
    {
        storeFloatComponentsRGB(target);
        target.a = this.alpha / 255.0f;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void storeFloatComponentsRGB(@NotNull RGB target)
    {
        target.r = this.red / 255.0f;
        target.g = this.green / 255.0f;
        target.b = this.blue / 255.0f;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void storeFloatComponentsHSBA(@NotNull HSBA target)
    {
        var hsb = this.getFloatComponentsHSBA();

        target.h = hsb.h;
        target.s = hsb.s;
        target.b = hsb.b;
        target.a = hsb.a;
    }

    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public void storeFloatComponentsHSB(@NotNull HSB target)
    {
        var hsb = this.getFloatComponentsHSB();

        target.h = hsb.h;
        target.s = hsb.s;
        target.b = hsb.b;
    }


    /**
     * TODO
     *
     * @since 20.2.0.0-alpha.3
     * @author 493msi
     */
    public java.awt.Color toAWT()
    {
        return new java.awt.Color(this.red, this.green, this.blue, this.alpha);
    }
}
