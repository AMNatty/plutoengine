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

package cz.tefek.srclone.particle;

import org.plutoengine.graphics.ImmediateFontRenderer;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.util.color.Color;
import org.plutoengine.util.color.RGBA;

import cz.tefek.srclone.SRCloneMod;

public class ParticleText extends Particle
{
    protected String text;

    public ParticleText(String text)
    {
        this.text = text;
        this.finalScale = 2.0f;
        this.size = 16.0f;
        this.initialLifetime = 2.0f;
    }

    @Override
    public void render()
    {
        var col = new RGBA(1.0f, 1.0f, 1.0f, Math.min(1.0f, 3.0f - 4.0f * this.animationTimer));

        var style = new TextStyleOptions(this.scale * this.size)
            .setPaint(LiPaint.solidColor(Color.from(col)))
            .setVerticalAlign(TextStyleOptions.TextAlign.CENTER)
            .setHorizontalAlign(TextStyleOptions.TextAlign.CENTER);

        ImmediateFontRenderer.drawString(this.getRenderX(), this.getRenderY(), this.text, SRCloneMod.srCloneFont, style);
    }
}
