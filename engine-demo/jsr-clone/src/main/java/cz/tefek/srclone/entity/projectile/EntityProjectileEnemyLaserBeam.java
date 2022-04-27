package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectileEnemyLaserBeam extends EntityProjectileLaserBeam
{
    public EntityProjectileEnemyLaserBeam()
    {
        super(EnumAmmo.E_LASER_BEAM);

        this.team = EnumTeam.ENEMY;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 512, 0, 256, 256);
        this.maxLifetime = 4.0f;
        this.velocity = 400.0f;
        this.damage = 5.0f;
    }
}
