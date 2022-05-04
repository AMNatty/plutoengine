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

import org.plutoengine.PlutoLocal;
import org.plutoengine.display.Display;
import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import java.util.Random;

import cz.tefek.srclone.SRCloneMod;

public class StarField
{
    private static final int STAR_COUNT = 512;

    private final Star[] stars;

    public StarField(float minX, float minY, float maxX, float maxY, RectangleTexture[] variations)
    {
        this.stars = new Star[STAR_COUNT];

        var random = new Random();

        for (int i = 0; i < STAR_COUNT; i++)
        {
            var x = minX + maxX * random.nextFloat();
            var y = minY + maxY * random.nextFloat();
            var z = random.nextFloat() * 0.9f + 0.1f;
            var texture = variations[random.nextInt(variations.length)];
            var size = random.nextFloat() * 20.0f + 10.0f;

            stars[i] = new Star(x, y, z, texture, size);
        }
    }

    public void render(float camX, float camY)
    {
        var renderer = RectangleRenderer2D.draw(SRCloneMod.centeredQuad);
        var display = PlutoLocal.components().getComponent(Display.class);

        var displayWidth = display.getWidth();
        var displayHeight = display.getHeight();

        for (var star : this.stars)
        {
            var size = star.size() * star.z();
            var x = star.x() + star.z() * camX;
            var y = star.y() + star.z() * camY;

            x = (x % displayWidth + displayWidth) % displayWidth;
            y = (y % displayHeight + displayHeight) % displayHeight;

            renderer.at(x, y, size, size);
            renderer.texture(star.texture());
            renderer.flush();
        }
    }

    private record Star(float x, float y, float z, RectangleTexture texture, float size)
    {

    }
}
