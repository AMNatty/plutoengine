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
