package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectilePlayerLaserBeam extends EntityProjectileLaserBeam
{
    public EntityProjectilePlayerLaserBeam()
    {
        super(EnumAmmo.P_LASER_BEAM);

        this.team = EnumTeam.PLAYER;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 1024, 0, 256, 256);
        this.maxLifetime = 4.0f;
        this.velocity = 1000.0f;
        this.damage = 20.0f;
    }
}
