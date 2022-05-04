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
