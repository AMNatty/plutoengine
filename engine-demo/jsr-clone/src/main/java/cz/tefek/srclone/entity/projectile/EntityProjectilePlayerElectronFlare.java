package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectilePlayerElectronFlare extends EntityProjectileAmmoSeeking
{
    public EntityProjectilePlayerElectronFlare()
    {
        super(EnumAmmo.P_ELECTRON_FLARE);

        this.team = EnumTeam.PLAYER;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 0, 0, 256, 256);
        this.size = 24.0f;
        this.ageOnHit = 0.5f;
        this.damage = 50.0f;
        this.velocity = 350.0f;
        this.maxLifetime = 5.0f;
        this.steeringRate = (float) Math.PI;
    }
}
