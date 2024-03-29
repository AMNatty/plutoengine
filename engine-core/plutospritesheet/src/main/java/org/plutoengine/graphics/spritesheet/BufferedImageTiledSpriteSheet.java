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

package org.plutoengine.graphics.spritesheet;

import org.plutoengine.graphics.sprite.Sprite;
import org.plutoengine.graphics.sprite.TileSprite;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

// FIXME
/**
 * A sprite atlas for {@link BufferedImage}.
 * 
 * @deprecated Completely untested, use at your own risk.
 * @author 493msi
 *
 */
@Deprecated
public class BufferedImageTiledSpriteSheet extends TiledSpriteSheet<BufferedImage>
{
    protected Graphics2D drawGraphics;

    public BufferedImageTiledSpriteSheet(int expectedTileSize, int spriteSheetSize)
    {
        super(expectedTileSize, spriteSheetSize);

        var size = this.spriteSheetWidth * this.tileWidth;
        this.spriteSheet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        this.drawGraphics = this.spriteSheet.createGraphics();
        this.drawGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }

    public BufferedImageTiledSpriteSheet(int tileSize)
    {
        this(tileSize, 8);
    }

    @Override
    public boolean requiresExpanding(int index)
    {
        return false;
    }

    @Override
    public void copyToNewImage()
    {
        this.drawGraphics.dispose();

        var newSpriteSheet = new BufferedImage(this.getWidthInPixels(), this.getHeightInPixels(), BufferedImage.TYPE_INT_ARGB);
        this.drawGraphics = newSpriteSheet.createGraphics();
        this.drawGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        var i = 0;

        for (var sprite : this.sprites)
        {
            if (sprite == null)
            {
                i++;
                continue;
            }

            var xPos = i % this.spriteSheetWidth * this.tileWidth;
            var yPos = i / this.spriteSheetWidth * this.tileHeight;

            this.drawTileSprite(sprite, xPos, yPos, this.tileWidth, this.tileHeight);

            sprite.setX(xPos);
            sprite.setY(yPos);
            sprite.setWidth(this.tileWidth);
            sprite.setHeight(this.tileHeight);

            i++;
        }

        this.spriteSheet = newSpriteSheet;
    }

    @Override
    public void close()
    {
        this.drawGraphics.dispose();
        this.drawGraphics = null;

        super.close();
    }

    @Override
    protected void drawTileSprite(TileSprite<TiledSpriteSheet<BufferedImage>> sprite, int x, int y, int width, int height)
    {
        var image = sprite.getSheet().getSpriteSheetImage();

        var sx1 = sprite.getX();
        var sy1 = sprite.getY();
        var sx2 = sx1 + sprite.getWidth();
        var sy2 = sy1 + sprite.getHeight();

        this.drawGraphics.drawImage(image, x, y, x + width, y + height, sx1, sy1, sx2, sy2, null);
    }

    @Override
    protected void drawSprite(Sprite<BufferedImage> sprite, int x, int y, int width, int height)
    {
        var image = sprite.getSheet();

        var sx1 = sprite.getX();
        var sy1 = sprite.getY();
        var sx2 = sx1 + sprite.getWidth();
        var sy2 = sy1 + sprite.getHeight();

        this.drawGraphics.drawImage(image, x, y, x + width, y + height, sx1, sy1, sx2, sy2, null);
    }
}
