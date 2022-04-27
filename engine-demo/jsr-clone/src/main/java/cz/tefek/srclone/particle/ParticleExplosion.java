package cz.tefek.srclone.particle;

import cz.tefek.srclone.SRCloneMod;

public class ParticleExplosion extends ParticleAnimatedSprite
{
    public ParticleExplosion()
    {
        super(SRCloneMod.parExplosion);
        this.finalScale = 1.1f;
        this.size = 256.0f;
        this.initialLifetime = 0.7f;
        this.maxRotationalVelocity = (float) Math.PI / 2.0f;
        this.lifetimeVariation = 0.2f;
    }
}
