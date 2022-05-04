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

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectilePlayerPolySwarm extends EntityProjectileAmmoSeeking
{
    public EntityProjectilePlayerPolySwarm()
    {
        super(EnumAmmo.P_POLY_SWARM);

        this.team = EnumTeam.PLAYER;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 1536, 0, 256, 256);
        this.size = 24.0f;
        this.ageOnHit = 10.0f;
        this.damage = 7.5f;
        this.velocity = 300.0f;
        this.maxLifetime = 10.0f;
        this.steeringRate = (float) Math.PI;
    }
}
