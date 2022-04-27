package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectilePlayerPlasmaDisc extends EntityProjectileAmmo
{
    protected float damageBase;

    public EntityProjectilePlayerPlasmaDisc()
    {
        super(EnumAmmo.P_PLASMA_DISC);

        this.team = EnumTeam.PLAYER;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 1280, 0, 256, 256);
        this.size = 24.0f;
        this.ageOnHit = 0.0f;
        this.damageBase = 150.0f;
        this.maxLifetime = 20.0f;
        this.velocity = 500.0f;
        this.velocityFalloff = 0.9f;
    }

    @Override
    public void tick(float delta)
    {
        float steeringRate = (float) Math.PI;
        this.rotation += steeringRate * delta;
        this.damage = this.damageBase * delta * this.velocity / 100.0f;

        super.tick(delta);
    }
}
