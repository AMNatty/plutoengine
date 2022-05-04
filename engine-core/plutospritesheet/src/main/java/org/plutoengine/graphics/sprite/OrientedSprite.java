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

package org.plutoengine.graphics.sprite;

import org.plutoengine.math.BasicInterpolation;

public class OrientedSprite
{
    protected PartialTextureSprite[] sides;

    public OrientedSprite(int sideCount)
    {
        this.sides = new PartialTextureSprite[sideCount];
    }

    public OrientedSprite(PartialTextureSprite[] sides)
    {
        this.sides = sides;
    }

    public void setSide(int side, PartialTextureSprite sprite)
    {
        this.sides[side] = sprite;
    }

    public int getSideCount()
    {
        return this.sides.length;
    }

    public PartialTextureSprite getSideFromAngle(float angle)
    {
        var side = BasicInterpolation.roundedLerpWrap(angle / (Math.PI * 2), 0, this.sides.length);
        return this.sides[side];
    }

    public PartialTextureSprite getSide(int i)
    {
        return this.sides[i];
    }

    public PartialTextureSprite[] getSides()
    {
        return this.sides;
    }
}
