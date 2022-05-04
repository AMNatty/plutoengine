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
    public RGBA toRGBA()
    {
        return this.toRGBA(this.a);
    }
}
