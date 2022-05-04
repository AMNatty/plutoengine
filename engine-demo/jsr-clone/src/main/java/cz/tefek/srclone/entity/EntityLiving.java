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

package cz.tefek.srclone.entity;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.Game;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.particle.ParticleExplosion;

public class EntityLiving extends Entity
{
    protected float health;

    protected float maxHealth;

    protected EntityLiving()
    {

    }

    @Override
    public void init(Game game, float x, float y)
    {
        super.init(game, x, y);
        this.health = this.maxHealth;
    }

    public void tick(float frameTime)
    {
        if (this.health <= 0)
        {
            this.deadFlag = true;
            this.onDie();
        }
    }

    public void heal(Entity source, float health)
    {
        this.health = Math.min(this.maxHealth, this.health + health);
        this.onHeal(source, health);
    }

    public void damage(Entity source, float health)
    {
        this.health -= health;
        this.onDamage(source, health);
    }

    public float getHealth()
    {
        return this.health;
    }

    public float getMaxHealth()
    {
        return this.maxHealth;
    }

    public void onHeal(Entity source, float amount)
    {

    }

    public void onDamage(Entity source, float amount)
    {

    }

    public void onDie()
    {
        var explosion = new ParticleExplosion();
        explosion.setSize(this.size * (this.team == EnumTeam.PLAYER ? 16.0f : 8.0f));
        this.game.addParticle(explosion, this.x, this.y);

        var rand = this.game.getRandom();
        var audioEngine = this.game.getAudioEngine();
        audioEngine.playSoundEffect(SRCloneMod.explosionSound[rand.nextInt(SRCloneMod.explosionSound.length)], this.x, this.y, 0.15f);
    }
}
