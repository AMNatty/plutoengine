package cz.tefek.srclone.particle;

import cz.tefek.srclone.SRCloneMod;

public class ParticleImpact extends ParticleAnimatedSprite
{
    public ParticleImpact()
    {
        super(SRCloneMod.parImpact);
        this.size = 64.0f;
        this.initialLifetime = 0.10f;
        this.maxRotationalVelocity = (float) Math.PI / 2.0f;
    }
}
