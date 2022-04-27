package cz.tefek.srclone.entity.projectile;

import org.plutoengine.graphics.sprite.PartialTextureSprite;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectilePlayerTachyonDisc extends EntityProjectileAmmo
{
    protected float damageBase;

    public EntityProjectilePlayerTachyonDisc()
    {
        super(EnumAmmo.P_TACHYON_DISC);

        this.team = EnumTeam.PLAYER;
        this.sprite = new PartialTextureSprite(SRCloneMod.projectilesBase, 1792, 0, 256, 256);
        this.size = 24.0f;
        this.ageOnHit = 0.0f;
        this.damageBase = 400.0f;
        this.maxLifetime = 10.0f;
        this.velocity = 100.0f;
        this.velocityFalloff = 4.0f;
    }

    @Override
    public void tick(float delta)
    {
        this.damage = this.damageBase * delta * this.velocity / 100.0f;

        super.tick(delta);
    }
}
