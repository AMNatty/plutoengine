package cz.tefek.srclone.entity.projectile;

import java.util.Comparator;

import cz.tefek.srclone.ammo.EnumAmmo;

public class EntityProjectileAmmoSeeking extends EntityProjectileAmmo
{
    protected float steeringRate;

    protected EntityProjectileAmmoSeeking(EnumAmmo ammo)
    {
        super(ammo);
    }

    @Override
    public void tick(float dt)
    {
        var target = this.game.getEntities()
                              .parallelStream()
                              .filter(entity -> entity.getTeam() != this.team)
                              .min(Comparator.comparing(this::getDistance));

        if (target.isPresent())
        {
            var entity = target.get();

            float dx = entity.getX() - this.x;
            float dy = entity.getY() - this.y;

            float targetAngle = (float) Math.atan2(dy, dx);

            float errorCorrection = targetAngle - this.rotation;

            if (errorCorrection > Math.PI)
                errorCorrection -= 2 * Math.PI;

            if (errorCorrection < -Math.PI)
                errorCorrection += 2 * Math.PI;

            float clampedErrorCorrection = Math.max(-this.steeringRate * dt, Math.min(this.steeringRate * dt, errorCorrection));
            this.rotation += clampedErrorCorrection;
        }
        else
        {
            this.rotation += this.steeringRate / 2.0f * dt;
        }

        super.tick(dt);
    }
}
