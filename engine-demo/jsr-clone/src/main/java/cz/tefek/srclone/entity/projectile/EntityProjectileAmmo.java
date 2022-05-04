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

import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.graphics.sprite.Sprite;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import cz.tefek.srclone.Game;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;
import cz.tefek.srclone.entity.Entity;
import cz.tefek.srclone.entity.EntityLiving;
import cz.tefek.srclone.particle.ParticleImpact;

public abstract class EntityProjectileAmmo extends EntityProjectile
{
    public static final float HIT_AUDIO_TICK_LIMIT = 0.075f;

    protected final EnumAmmo ammo;

    protected float ageOnHit;

    protected float damage;

    protected float maxLifetime;
    protected float velocity;

    protected Sprite<RectangleTexture> sprite;

    protected float visualSize;

    protected float hitSoundTimer = 0.0f;

    protected float velocityFalloff;

    protected EntityProjectileAmmo(EnumAmmo ammo)
    {
        this.ammo = ammo;
        this.visualSize = 64.0f;
        this.ageOnHit = 4.0f;
        this.velocityFalloff = 0.95f;
    }

    @Override
    public void init(Game game, float x, float y)
    {
        super.init(game, x, y);

        var rand = this.game.getRandom();
        this.emitSoundEffect(this.ammo.getShootSound(), 0.30f, 0.9f + rand.nextFloat() * 0.2f, false);
    }

    @Override
    public void tick(float delta)
    {
        this.velocity *= Math.pow(this.velocityFalloff, delta);

        float vx = (float) (this.velocity * Math.cos(this.rotation));
        float vy = (float) (this.velocity * Math.sin(this.rotation));

        this.x += vx * delta;
        this.y += vy * delta;

        if (this.lifetime >= this.maxLifetime)
        {
            this.deadFlag = true;
            return;
        }

        this.hitSoundTimer -= delta;

        super.tick(delta);
    }

    @Override
    public void render()
    {
        RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
            .at(this.getRenderX(), this.getRenderY(), this.visualSize, this.visualSize)
            .rotate(this.rotation + (float) Math.PI / 4.0f)
            .sprite(this.sprite)
            .flush();
    }

    @Override
    protected void onHit(Entity entity)
    {
        this.lifetime += this.ageOnHit;

        if (this.lifetime >= this.maxLifetime)
            this.deadFlag = true;

        if (entity instanceof EntityLiving entityLiving)
        {
            entityLiving.damage(this, this.damage);
        }

        var hitParticle = new ParticleImpact();
        hitParticle.setRotation(this.rotation - 1.75f * (float) Math.PI);
        this.game.addParticle(hitParticle, this.x, this.y);

        if (this.hitSoundTimer <= 0)
        {
            var audioEngine = this.game.getAudioEngine();
            audioEngine.playSoundEffect(SRCloneMod.impactSound, this.x, this.y, 0.15f);

            this.hitSoundTimer = HIT_AUDIO_TICK_LIMIT;
        }

        super.onHit(entity);
    }
}
