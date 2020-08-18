package cz.tefek.pluto.engine.graphics.spritesheet;

import java.util.Vector;

import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.Severity;
import cz.tefek.pluto.engine.graphics.sprite.Sprite;
import cz.tefek.pluto.engine.graphics.sprite.SpriteDisposable;
import cz.tefek.pluto.engine.graphics.sprite.TileSprite;

public abstract class TiledSpriteSheet<T> extends SpriteSheet<T>
{
    protected Vector<TileSprite<TiledSpriteSheet<T>>> sprites;

    protected static int idCounter = 1;

    protected int id = idCounter++;

    protected int tileWidth;
    protected int tileHeight;

    protected final float aspectRatio;

    protected int spriteSheetWidth;
    protected int spriteSheetHeight;

    private static final int approxStarterSpriteSheetSize = 8;
    private static final int approxStarterSpriteCount = approxStarterSpriteSheetSize * approxStarterSpriteSheetSize;

    public TiledSpriteSheet(int tileWidth, int tileHeight)
    {

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.aspectRatio = (float) tileWidth / tileHeight;

        this.spriteSheetWidth = Math.round(approxStarterSpriteCount / (approxStarterSpriteSheetSize / this.aspectRatio));
        this.spriteSheetHeight = Math.round(approxStarterSpriteCount / (approxStarterSpriteSheetSize * this.aspectRatio));

        this.sprites = new Vector<>(this.spriteSheetWidth * this.spriteSheetHeight);
        this.sprites.setSize(this.spriteSheetWidth * this.spriteSheetHeight);
    }

    protected abstract void drawTileSprite(TileSprite<TiledSpriteSheet<T>> sprite, int x, int y, int width, int height);

    protected abstract void drawSprite(Sprite<T> sprite, int x, int y, int width, int height);

    @Override
    public int getWidthInPixels()
    {
        return this.tileWidth * this.spriteSheetWidth;
    }

    @Override
    public int getHeightInPixels()
    {
        return this.tileHeight * this.spriteSheetHeight;
    }

    protected boolean requiresExpanding(int index)
    {
        return index >= this.sprites.size();
    }

    protected void expand()
    {
        Logger.logf(Severity.INFO, "Spritesheet #%d: Expanding from %dx%d to ", this.id, this.spriteSheetWidth, this.spriteSheetHeight);

        this.spriteSheetWidth *= 2;
        this.spriteSheetHeight *= 2;

        this.sprites.setSize(this.spriteSheetWidth * this.spriteSheetHeight);

        Logger.logf("%dx%d\n", this.spriteSheetWidth, this.spriteSheetHeight);

        this.copyToNewImage();
    }

    protected void upscale(int factor)
    {
        Logger.logf(Severity.INFO, "Spritesheet #%d: Upscaling from %dx%d to ", this.id, this.tileWidth, this.tileHeight);

        this.tileWidth *= factor;
        this.tileHeight *= factor;

        Logger.logf("%dx%d\n", this.tileWidth, this.tileHeight);

        this.copyToNewImage();
    }

    @Override
    public void delete()
    {
        this.sprites.clear();
        this.sprites = null;

        this.spriteSheet = null;
    }

    public abstract void copyToNewImage();

    public TileSprite<TiledSpriteSheet<T>> addSprite(Sprite<T> sprite, int index)
    {
        if (!this.aspectRatiosMatch(sprite))
        {
            throw new IllegalArgumentException("Sprite and spritesheet aspect ratios do not match!");
        }

        if (!this.isMultiple(sprite))
        {
            throw new IllegalArgumentException("The sprite and the spritesheet do not have a common resolution to scale to.");
        }

        if (sprite.getWidth() > this.tileWidth)
        {
            this.upscale(sprite.getWidth() / this.tileWidth);
        }

        while (this.requiresExpanding(index))
        {
            this.expand();
        }

        var newX = index % this.spriteSheetWidth * this.tileWidth;
        var newY = index / this.spriteSheetWidth * this.tileHeight;
        var newWidth = this.tileWidth;
        var newHeight = this.tileHeight;

        this.drawSprite(sprite, newX, newY, newWidth, newHeight);

        if (sprite instanceof SpriteDisposable<?>)
        {
            var disposableSprite = (SpriteDisposable<?>) sprite;
            disposableSprite.delete();
        }

        var copySprite = new TileSprite<TiledSpriteSheet<T>>(newX, newY, newWidth, newHeight);
        copySprite.setSpriteSheet(this);

        this.sprites.set(index, copySprite);

        return copySprite;
    }

    private boolean aspectRatiosMatch(Sprite<?> sprite)
    {
        final var epsilon = 0.001f;

        return Math.abs(sprite.getWidth() - this.aspectRatio * sprite.getHeight()) < epsilon;
    }

    private boolean isMultiple(Sprite<?> sprite)
    {
        final var spriteWidth = sprite.getWidth();
        final var spriteHeight = sprite.getHeight();

        // Shortcut for images with matching sizes;
        if (spriteWidth == this.tileWidth && spriteHeight == this.tileHeight)
        {
            return true;
        }

        if (this.tileWidth < spriteWidth)
        {
            int i = this.tileWidth;

            while (i < spriteWidth)
            {
                i <<= 1;
            }

            if (i != spriteWidth)
            {
                return false;
            }
        }
        else
        {
            int i = spriteWidth;

            while (i < this.tileWidth)
            {
                i <<= 1;
            }

            if (i != this.tileWidth)
            {
                return false;
            }
        }

        if (this.tileHeight < spriteHeight)
        {
            int i = this.tileHeight;

            while (i < spriteHeight)
            {
                i <<= 1;
            }

            if (i != spriteHeight)
            {
                return false;
            }
        }
        else
        {
            int i = spriteHeight;

            while (i < this.tileHeight)
            {
                i <<= 1;
            }

            if (i != this.tileHeight)
            {
                return false;
            }
        }

        return true;
    }

    public int getTileWidth()
    {
        return this.tileWidth;
    }

    public int getTileHeight()
    {
        return this.tileHeight;
    }

    public int getSpritesPerRow()
    {
        return this.spriteSheetWidth;
    }

    public int getSpritesPerColumn()
    {
        return this.spriteSheetHeight;
    }
}
