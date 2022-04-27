package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectilePlayerHeatStar extends EntityProjectileAmmo
{
    public EntityProjectilePlayerHeatStar()
    {
        super(EnumAmmo.P_HEAT_STAR);

        this.team = EnumTeam.PLAYER;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 768, 0, 256, 256);
        this.maxLifetime = 8.0f;
        this.velocity = 250.0f;
        this.damage = 200.0f;
    }
}
