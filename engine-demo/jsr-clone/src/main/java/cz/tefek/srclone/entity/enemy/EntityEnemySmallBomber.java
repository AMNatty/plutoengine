/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cz.tefek.srclone.entity.enemy;

import org.plutoengine.graphics.RectangleRenderer2D;

import cz.tefek.srclone.Game;
import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.ammo.EnumAmmo;
import cz.tefek.srclone.entity.pickup.EntityBox;
import cz.tefek.srclone.util.AngleUtil;

public class EntityEnemySmallBomber extends EntityEnemy
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

    public EntityEnemySmallBomber()
    {
        this.maxHealth = 150.0f;
        this.size = 32.0f;
        this.speed = 180.0f;
        this.ammo = EnumAmmo.E_HEAT_STAR;
        this.movementChangeInterval = 0.3f;
        this.precision = (float) (Math.PI / 80.0f);
        this.fleeRange = 350.0f;
        this.turningRate = (float) Math.PI;
    }

    @Override
    public void init(Game game, float x, float y)
    {
        super.init(game, x, y);
        this.movementChangeTimer = this.movementChangeInterval;
        var rand = this.game.getRandom();
        this.weaponCooldown = this.ammo.getCooldown() * (9.0f + rand.nextFloat() * 3.0f);    }

    @Override
    public void tick(float delta)
    {
        this.movementChangeTimer -= delta;

        var player = this.game.getEntityPlayer();

        float dx = player.getX() - this.x;
        float dy = player.getY() - this.y;

        var rand = this.game.getRandom();

        float generalAngle = (float) Math.atan2(dx, dy);

        if (this.movementChangeTimer <= 0.0f)
        {
            this.targetAngle = generalAngle + this.precision * (rand.nextFloat() - 0.5f);

            this.movementChangeTimer += this.movementChangeInterval * (1.0f + rand.nextFloat() * 2.0f);
        }

        if (player.getDistance(this) < this.fleeRange)
            this.targetAngle = (float) (Math.atan2(dx, dy) - Math.PI);

        float errorCorrection = AngleUtil.within180(this.targetAngle - this.rotation);

        float clampedErrorCorrection = Math.max(-this.turningRate * delta, Math.min(this.turningRate * delta, errorCorrection));
        this.rotation += clampedErrorCorrection;

        this.x += Math.cos(-this.rotation + Math.PI / 2.0) * this.speed * delta;
        this.y += Math.sin(-this.rotation + Math.PI / 2.0) * this.speed * delta;

        this.weaponCooldown -= delta;

        if (this.weaponCooldown <= 0 && Math.abs(AngleUtil.within180(generalAngle - this.rotation)) < Math.PI / 25.0f)
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

        float engineOffset = 15;

        float engineX = this.getRenderX() - (float) (Math.sin(this.rotation) * engineOffset);
        float engineY = this.getRenderY() - (float) (Math.cos(this.rotation) * engineOffset);

        float nozzleRot = (float) (-this.rotation + Math.PI / 4.0f * 3.0f);

        if (Math.cos(this.rotation) > 0.3f)
        {
            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(engineX, engineY, w, h)
                               .rotate(AngleUtil.snapToDirections(nozzleRot, SRCloneMod.enemySmallBomber.getSideCount()))
                               .recolor(1.0f, 0.0f, 1.0f, 0.3f)
                               .texture(SRCloneMod.rocketNozzle)
                               .flush();

            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(this.getRenderX(), this.getRenderY(), w, h)
                               .sprite(SRCloneMod.enemySmallBomber.getSideFromAngle(this.rotation))
                               .flush();
        }
        else
        {

            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(this.getRenderX(), this.getRenderY(), w, h)
                               .sprite(SRCloneMod.enemySmallBomber.getSideFromAngle(this.rotation))
                               .flush();

            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(engineX, engineY, w, h)
                               .rotate(AngleUtil.snapToDirections(nozzleRot, SRCloneMod.enemySmallBomber.getSideCount()))
                               .recolor(1.0f, 0.0f, 1.0f, 0.3f)
                               .texture(SRCloneMod.rocketNozzle)
                               .flush();
        }
    }

    @Override
    public void onDie()
    {
        if (this.game.getRandom().nextFloat() < 0.2f)
            this.game.addEntity(new EntityBox(), this.x, this.y);

        super.onDie();
    }
}
