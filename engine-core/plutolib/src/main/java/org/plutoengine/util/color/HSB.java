/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine.util.color;

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
    public RGBA toRGBA()
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
        // The second-nearest color component on the hue wheel - counter-clockwise
        float q = this.b * (1 - hueFractCCW * this.s);

        float hueFractCW = 1 - hueFractCCW;
        // The second-nearest color component on the hue wheel - clockwise
        float t = this.b * (1 - hueFractCW * this.s);

        return switch (hueSide % 6)
        {
            // Hues 60°-119° -- Green is the brightest color, no blue is present at max saturation
            case 1 -> new RGBA(q, this.b, p, alpha);
            // Hues 120°-179° -- Green is the brightest color, no red is present at max saturation
            case 2 -> new RGBA(p, this.b, t, alpha);
            // Hues 180°-239° -- Blue is the brightest color, no red is present at max saturation
            case 3 -> new RGBA(p, q, this.b, alpha);
            // Hues 240°-299° -- Blue is the brightest color, no green is present at max saturation
            case 4 -> new RGBA(t, p, this.b, alpha);
            // Hues 300°-359° -- Red is the brightest color, no green is present at max saturation
            case 5 -> new RGBA(this.b, p, q, alpha);
            // Hues 0°-59° -- Red is the brightest color, no blue is present at max saturation
            case 0 -> new RGBA(this.b, t, p, alpha);

            default -> throw new IllegalStateException("This HSB object's hue is negative - this is not legal.");
        };
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
