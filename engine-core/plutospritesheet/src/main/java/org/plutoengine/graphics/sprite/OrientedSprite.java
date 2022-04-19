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
