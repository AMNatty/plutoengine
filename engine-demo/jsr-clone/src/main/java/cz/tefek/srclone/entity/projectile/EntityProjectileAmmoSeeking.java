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

package cz.tefek.srclone.entity.projectile;

import java.util.Comparator;

import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectileAmmoSeeking extends EntityProjectileAmmo
{
    protected float steeringRate;

    protected EntityProjectileAmmoSeeking(EnumAmmo ammo)
    {
        super(ammo);
    }

    @Override
    public void tick(float dt)
    {
        var target = this.game.getEntities()
                              .parallelStream()
                              .filter(entity -> entity.getTeam() != this.team)
                              .min(Comparator.comparing(this::getDistance));

        if (target.isPresent())
        {
            var entity = target.get();

            float dx = entity.getX() - this.x;
            float dy = entity.getY() - this.y;

            float targetAngle = (float) Math.atan2(dy, dx);

            float errorCorrection = targetAngle - this.rotation;

            if (errorCorrection > Math.PI)
                errorCorrection -= 2 * Math.PI;

            if (errorCorrection < -Math.PI)
                errorCorrection += 2 * Math.PI;

            float clampedErrorCorrection = Math.max(-this.steeringRate * dt, Math.min(this.steeringRate * dt, errorCorrection));
            this.rotation += clampedErrorCorrection;
        }
        else
        {
            this.rotation += this.steeringRate / 2.0f * dt;
        }

        super.tick(dt);
    }
}
