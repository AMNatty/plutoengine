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

import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.math.BasicInterpolation;

import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.graphics.DirectionalSprite;

public class ParticleAnimatedSprite extends Particle
{
    protected DirectionalSprite sprite;

    public ParticleAnimatedSprite(DirectionalSprite sprite)
    {
        this.sprite = sprite;
    }

    @Override
    public void render()
    {
        var size = this.size * this.scale;

        RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                           .at(this.getRenderX(), this.getRenderY(), size, size)
                           .rotate(this.rotation)
                           .sprite(this.sprite.getSide(BasicInterpolation.floorLerp(this.animationTimer, 0, this.sprite.getSideCount() - 1)))
                           .flush();
    }
}
