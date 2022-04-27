package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectileEnemyHeatStar extends EntityProjectileHeatStar
{
    public EntityProjectileEnemyHeatStar()
    {
        super(EnumAmmo.E_HEAT_STAR);

        this.team = EnumTeam.ENEMY;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 256, 0, 256, 256);
        this.maxLifetime = 4.0f;
        this.velocity = 200.0f;
        this.damage = 20.0f;
    }
}
