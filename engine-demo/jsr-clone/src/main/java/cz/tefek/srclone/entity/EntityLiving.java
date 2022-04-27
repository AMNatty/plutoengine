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
