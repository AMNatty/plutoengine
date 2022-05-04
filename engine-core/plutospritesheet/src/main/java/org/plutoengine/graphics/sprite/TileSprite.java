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

import org.plutoengine.graphics.spritesheet.TiledSpriteSheet;

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
