package org.plutoengine.graphics.sprite;

import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

public class PartialTextureSprite implements Sprite<RectangleTexture>
{
    protected transient RectangleTexture spriteTexture;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public PartialTextureSprite(RectangleTexture texture, int x, int y, int width, int height)
    {
        this.spriteTexture = texture;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public PartialTextureSprite(int x, int y, int width, int height)
    {
        this(null, x, y, width, height);
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
    }

    @Override
    public int getHeight()
    {
        return this.height;
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    public void setSheet(RectangleTexture spriteTexture)
    {
        this.spriteTexture = spriteTexture;
    }

    @Override
    public RectangleTexture getSheet()
    {
        return this.spriteTexture;
    }

}
