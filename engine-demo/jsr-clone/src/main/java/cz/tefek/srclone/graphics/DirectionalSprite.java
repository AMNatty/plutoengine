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

package cz.tefek.srclone.graphics;

import org.plutoengine.graphics.sprite.OrientedSprite;
import org.plutoengine.graphics.sprite.PartialTextureSprite;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import java.nio.file.Path;

public final class DirectionalSprite extends OrientedSprite implements AutoCloseable
{
    private final RectangleTexture backingTexture;

    private DirectionalSprite(RectangleTexture backingTexture, PartialTextureSprite[] sprite)
    {
        super(sprite);
        this.backingTexture = backingTexture;
    }

    public static DirectionalSprite create(Path path, int dimensions, int width, int height, int stride)
    {
        var backingTexture = new RectangleTexture();
        backingTexture.load(path, MagFilter.NEAREST, MinFilter.LINEAR, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);

        var sprites = new PartialTextureSprite[dimensions];

        for (int i = 0; i < sprites.length; i++)
            sprites[i] = new PartialTextureSprite(backingTexture, i % stride * width, i / stride * height, width, height);

        return new DirectionalSprite(backingTexture, sprites);
    }

    @Override
    public void close()
    {
        this.backingTexture.close();
    }
}
