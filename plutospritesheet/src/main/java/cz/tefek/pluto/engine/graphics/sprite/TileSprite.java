package cz.tefek.pluto.engine.graphics.sprite;

import cz.tefek.pluto.engine.graphics.spritesheet.TiledSpriteSheet;

public class TileSprite<T extends TiledSpriteSheet<?>> implements Sprite<T>
{
    protected T spriteSheet;

    public static final int IMPLICIT_POSITION = -1;
    public static final int IMPLICIT_SIZE = -1;

    protected int x;
    protected int y;

    protected int width;
    protected int height;

    public TileSprite(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public TileSprite()
    {
        this(IMPLICIT_POSITION, IMPLICIT_POSITION, IMPLICIT_SIZE, IMPLICIT_SIZE);
    }

    @Override
    public int getX()
    {
        return this.x == IMPLICIT_POSITION ? 0 : this.x;
    }

    @Override
    public int getY()
    {
        return this.y == IMPLICIT_POSITION ? 0 : this.y;
    }

    @Override
    public int getWidth()
    {
        return this.width == IMPLICIT_SIZE ? this.spriteSheet.getWidthInPixels() : this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height == IMPLICIT_SIZE ? this.spriteSheet.getHeightInPixels() : this.height;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setSpriteSheet(T spriteSheet)
    {
        this.spriteSheet = spriteSheet;
    }

    @Override
    public T getSheet()
    {
        return this.spriteSheet;
    }
}
