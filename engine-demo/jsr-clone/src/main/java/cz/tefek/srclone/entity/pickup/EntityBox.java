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

package cz.tefek.srclone.entity.pickup;

import org.plutoengine.display.Framerate;
import org.plutoengine.graphics.RectangleRenderer2D;

import cz.tefek.srclone.ammo.EnumAmmo;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.entity.EntityPlayer;
import cz.tefek.srclone.particle.ParticleText;

public class EntityBox extends EntityPickup
{
    protected float maxLifetime;

    public EntityBox()
    {
        this.size = 32.0f;
        this.maxLifetime = 22.5f;
    }

    @Override
    public void tick(float delta)
    {
        if (this.lifetime >= this.maxLifetime)
            this.deadFlag = true;

        super.tick(delta);
    }

    @Override
    public void render()
    {
        var opacity = (1.0f - this.lifetime / this.maxLifetime) * 5.0f;
        opacity = Math.min(1.0f, Math.max(0.0f, opacity));

        float w = 64, h = 64;
        RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                           .at(this.getRenderX(), this.getRenderY(), w, h)
                           .sprite(SRCloneMod.pickupBox.getSideFromAngle((float) Math.PI * Framerate.getAnimationTimer()))
                           .recolor(1.0f, 1.0f, 1.0f, opacity)
                           .flush();
    }

    @Override
    protected void onPickup(EntityPlayer player)
    {
        var rand = this.game.getRandom();

        double healChance = rand.nextDouble();
        double r = rand.nextDouble();
        double mul = rand.nextDouble();

        String lootText;

        if (healChance < 0.2 && player.getHealth() < player.getMaxHealth())
        {
            float healAmount = 15.0f;
            lootText = "+%.0f\uD83D\uDC96".formatted(healAmount);

            player.heal(this, healAmount);
        }
        else
        {
            EnumAmmo ammo;
            long count;

            if (r > 0.95)
            {
                ammo = EnumAmmo.P_TACHYON_DISC;
                count = Math.round(5 + 3 * mul);
            }
            else if (r > 0.85)
            {
                ammo = EnumAmmo.P_ELECTRON_FLARE;
                count = Math.round(4 + 2 * mul);
            }
            else if (r > 0.70)
            {
                ammo = EnumAmmo.P_PLASMA_DISC;
                count = Math.round(3 + 3 * mul);
            }
            else if (r > 0.40)
            {
                ammo = EnumAmmo.P_HEAT_STAR;
                count = Math.round(20 + 20 * mul);
            }
            else
            {
                ammo = EnumAmmo.P_POLY_SWARM;
                count = Math.round(100 + 50 * mul);
            }

            player.addAmmo(ammo, count);
            lootText = "+%d%s".formatted(count, ammo.getTextIcon());
        }

        this.game.addParticle(new ParticleText(lootText), this.x, this.y);
    }
}
