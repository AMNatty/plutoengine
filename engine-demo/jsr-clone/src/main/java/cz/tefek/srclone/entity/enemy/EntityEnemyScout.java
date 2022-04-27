package cz.tefek.srclone.entity.enemy;

import org.plutoengine.graphics.RectangleRenderer2D;

import cz.tefek.srclone.Game;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;
import cz.tefek.srclone.entity.pickup.EntityBox;

public class EntityEnemyScout extends EntityEnemy
{
    protected float movementChangeTimer;
    protected float movementChangeInterval;

    protected float speed;

    protected float weaponCooldown;

    protected float targetAngle;

    protected float precision;

    protected float fleeRange;

    protected float turningRate;

    protected EnumAmmo ammo;

    public EntityEnemyScout()
    {
        this.maxHealth = 50.0f;
        this.size = 32.0f;
        this.speed = 90.0f;
        this.ammo = EnumAmmo.E_LASER_BEAM;
        this.movementChangeInterval = 0.5f;
        this.precision = (float) (Math.PI / 4.0f);
        this.fleeRange = 200.0f;
        this.turningRate = (float) (Math.PI / 2.0f);
    }

    @Override
    public void init(Game game, float x, float y)
    {
        super.init(game, x, y);
        this.movementChangeTimer = this.movementChangeInterval;
        var rand = this.game.getRandom();
        this.weaponCooldown = this.ammo.getCooldown() * (9.0f + rand.nextFloat() * 3.0f);
    }

    @Override
    public void tick(float delta)
    {
        this.movementChangeTimer -= delta;

        var player = this.game.getEntityPlayer();

        float dx = player.getX() - this.x;
        float dy = player.getY() - this.y;

        var rand = this.game.getRandom();

        if (this.movementChangeTimer <= 0.0f)
        {
            float generalAngle = (float) Math.atan2(dx, dy);
            this.targetAngle = generalAngle + this.precision * (rand.nextFloat() - 0.5f);

            this.movementChangeTimer += this.movementChangeInterval * (1.0f + rand.nextFloat() * 2.0f);
        }

        if (player.getDistance(this) < this.fleeRange)
            this.targetAngle = (float) (Math.atan2(dx, dy) - Math.PI);

        float errorCorrection = (float) ((this.targetAngle - this.rotation) % (2 * Math.PI));

        if (errorCorrection > Math.PI)
            errorCorrection -= 2 * Math.PI;

        if (errorCorrection < -Math.PI)
            errorCorrection += 2 * Math.PI;

        float clampedErrorCorrection = Math.max(-this.turningRate * delta, Math.min(this.turningRate * delta, errorCorrection));
        this.rotation += clampedErrorCorrection;

        this.x += Math.cos(-this.rotation + Math.PI / 2.0) * this.speed * delta;
        this.y += Math.sin(-this.rotation + Math.PI / 2.0) * this.speed * delta;

        this.weaponCooldown -= delta;

        if (this.weaponCooldown <= 0)
        {
            var projectile = this.ammo.createProjectile();
            float rot = (float) (-this.rotation + Math.PI / 2.0f);
            projectile.setRotation(rot);
            this.game.addEntity(projectile, this.x + this.size * (float) Math.sin(this.rotation), this.y + this.size * (float) Math.cos(this.rotation));

            this.weaponCooldown = this.ammo.getCooldown();
        }

        super.tick(delta);
    }

    @Override
    public void render()
    {
        float w = 128, h = 128;

        RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                           .at(this.getRenderX(), this.getRenderY(), w, h)
                           .sprite(SRCloneMod.enemyScout.getSideFromAngle(this.rotation))
                           .flush();
    }

    @Override
    public void onDie()
    {
        if (this.game.getRandom().nextFloat() < 0.05f)
            this.game.addEntity(new EntityBox(), this.x, this.y);

        super.onDie();
    }
}
