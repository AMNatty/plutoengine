package org.plutoengine.graphics.spritesheet;

public abstract class SpriteSheet<T>
{
    protected T spriteSheet;

    public SpriteSheet(T sourceImage)
    {
        this.spriteSheet = sourceImage;
    }

    public SpriteSheet()
    {

    }

    public void setSpriteSheetImage(T sourceImage)
    {
        this.spriteSheet = sourceImage;
    }

    public T getSpriteSheetImage()
    {
        return this.spriteSheet;
    }

    public abstract int getWidthInPixels();

    public abstract int getHeightInPixels();

    public abstract void delete();
}
