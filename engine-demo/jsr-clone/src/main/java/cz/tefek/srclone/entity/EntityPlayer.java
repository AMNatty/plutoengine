package cz.tefek.srclone.entity;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.plutoengine.PlutoLocal;
import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.input.Keyboard;
import org.plutoengine.math.BasicInterpolation;

import java.util.EnumMap;
import java.util.Map;

import cz.tefek.srclone.ammo.EnumAmmo;
import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.SRCloneMod;

public class EntityPlayer extends EntityLiving
{
    private float xSpeed;
    private float ySpeed;

    private float enginePower;

    private float weaponCooldown;

    private double score;

    private EnumAmmo selectedAmmo;

    private final Map<EnumAmmo, Long> ammo;

    public EntityPlayer()
    {
        this.size = 16;
        this.team = EnumTeam.PLAYER;
        this.ammo = new EnumMap<>(EnumAmmo.class);
        this.maxHealth = 100.0f;
        this.selectedAmmo = EnumAmmo.P_LASER_BEAM;
    }

    @Override
    public void render()
    {
        var sides = SRCloneMod.player.getSideCount();
        var side = BasicInterpolation.roundedLerpWrap(this.rotation / (Math.PI * 2), 0, sides);

        float w = 64, h = 64;

        float engineOffset = 15;

        float engineX = this.getRenderX() - (float) (Math.sin(this.rotation) * engineOffset);
        float engineY = this.getRenderY() - (float) (Math.cos(this.rotation) * engineOffset);

        float nozzleRot = (float) Math.toRadians(-side * 360.0f / sides + 135.0f);

        if (side >= sides / 4 * 3 || side <= sides / 4)
        {
            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(engineX, engineY, w, h)
                               .rotate(nozzleRot)
                               .recolor(1.0f, 1.0f, 1.0f, this.enginePower)
                               .texture(SRCloneMod.rocketNozzle)
                               .flush();

            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(this.getRenderX(), this.getRenderY(), w, h)
                               .sprite(SRCloneMod.player.getSide(side))
                               .flush();
        }
        else
        {

            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(this.getRenderX(), this.getRenderY(), w, h)
                               .sprite(SRCloneMod.player.getSide(side))
                               .flush();

            RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                               .at(engineX, engineY, w, h)
                               .rotate(nozzleRot)
                               .recolor(1.0f, 1.0f, 1.0f, this.enginePower)
                               .texture(SRCloneMod.rocketNozzle)
                               .flush();
        }
    }

    public void addScore(double score)
    {
        this.score += score;
    }

    public double getScore()
    {
        return this.score;
    }

    public void addAmmo(EnumAmmo ammo, long amount)
    {
        this.ammo.merge(ammo, amount, (oldCnt, add) -> oldCnt == EnumAmmo.AMMO_INFINITE ? EnumAmmo.AMMO_INFINITE : oldCnt + add);
    }

    public long getAmmo(EnumAmmo ammo)
    {
        return this.ammo.getOrDefault(ammo, 0L);
    }

    public EnumAmmo getSelectedAmmo()
    {
        return this.selectedAmmo;
    }

    @Override
    public void tick(float frameTime)
    {
        var keyboard = PlutoLocal.components().getComponent(Keyboard.class);

        this.tickShoot(keyboard, frameTime);
        this.tickWeaponSelect(keyboard);
        this.tickMovement(keyboard, frameTime);

        super.tick(frameTime);
    }

    private void tickShoot(Keyboard keyboard, float frameTime)
    {
        this.weaponCooldown -= frameTime;

        if (this.weaponCooldown <= 0 && keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE))
        {
            var ammoCount = this.getAmmo(this.selectedAmmo);

            if (ammoCount > 0)
            {
                var projectile = this.selectedAmmo.createProjectile();
                float rot = (float) (-this.rotation + Math.PI / 2.0f);
                projectile.setRotation(rot);

                var xPos = this.x + this.size * (float) Math.sin(this.rotation);
                var yPos = this.y + this.size * (float) Math.cos(this.rotation);
                this.game.addEntity(projectile, xPos, yPos);

                this.weaponCooldown += this.selectedAmmo.getCooldown();

                this.addAmmo(this.selectedAmmo, -1);
            }

            var newCount = this.getAmmo(this.selectedAmmo);

            if (newCount == 0)
            {
                var first = this.selectedAmmo;

                do
                    this.selectedAmmo = EnumAmmo.next(this.selectedAmmo);
                while (this.selectedAmmo != first && this.getAmmo(this.selectedAmmo) == 0);
            }
        }

        this.weaponCooldown = Math.max(this.weaponCooldown, 0.0f);
    }

    private void tickWeaponSelect(Keyboard keyboard)
    {
        if (keyboard.pressed(GLFW.GLFW_KEY_LEFT_CONTROL) || keyboard.pressed(GLFW.GLFW_KEY_Q))
        {
            var first = this.selectedAmmo;

            do
                this.selectedAmmo = EnumAmmo.previous(this.selectedAmmo);
            while (this.selectedAmmo != first && this.getAmmo(this.selectedAmmo) == 0);
        }
        else if (keyboard.pressed(GLFW.GLFW_KEY_LEFT_SHIFT) || keyboard.pressed(GLFW.GLFW_KEY_E))
        {
            var first = this.selectedAmmo;

            do
                this.selectedAmmo = EnumAmmo.next(this.selectedAmmo);
            while (this.selectedAmmo != first && this.getAmmo(this.selectedAmmo) == 0);
        }

        if (keyboard.pressed(GLFW.GLFW_KEY_L))
            this.damage(this, 100);
    }

    private void tickMovement(Keyboard keyboard, float frameTime)
    {
        float maxSpeed = 170;
        float accel = 400 * frameTime;

        var joystick = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1);
        var joystickX = 0.0f;
        var joystickY = 0.0f;

        var deadZone = 0.2f;

        if (joystick != null)
        {
            if (joystick.limit() >= 2)
            {
                joystickX = joystick.get(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
                joystickY = joystick.get(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);

                if (Math.abs(joystickX) < deadZone)
                    joystickX = 0;

                if (Math.abs(joystickY) < deadZone)
                    joystickY = 0;
            }
        }

        float friction = (float) Math.pow(0.05f, frameTime);

        var movementAxes = new Vector2f();

        if (keyboard.isKeyDown(GLFW.GLFW_KEY_UP) || keyboard.isKeyDown(GLFW.GLFW_KEY_W))
            movementAxes.y = -1;
        else if (keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN) || keyboard.isKeyDown(GLFW.GLFW_KEY_S))
            movementAxes.y = 1;
        else
            movementAxes.y = joystickY;

        if (keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT) || keyboard.isKeyDown(GLFW.GLFW_KEY_A))
            movementAxes.x = -1;
        else if (keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT) || keyboard.isKeyDown(GLFW.GLFW_KEY_D))
            movementAxes.x = 1;
        else
            movementAxes.x = joystickX;

        if (movementAxes.lengthSquared() > 1.0f)
            movementAxes.normalize();

        var xAccel = movementAxes.x * accel;
        var yAccel = movementAxes.y * accel;

        this.enginePower = movementAxes.lengthSquared();

        if (Math.signum(xAccel) != Math.signum(this.xSpeed))
            this.xSpeed *= friction;

        if (Math.signum(yAccel) != Math.signum(this.ySpeed))
            this.ySpeed *= friction;

        this.xSpeed += xAccel;
        this.ySpeed += yAccel;

        this.xSpeed = Math.max(Math.min(maxSpeed, this.xSpeed), -maxSpeed);
        this.ySpeed = Math.max(Math.min(maxSpeed, this.ySpeed), -maxSpeed);

        this.x += this.xSpeed * frameTime;
        this.y += this.ySpeed * frameTime;

        if (this.xSpeed != 0 || this.ySpeed != 0)
            this.rotation = (float) (Math.PI - Math.atan2(this.xSpeed, -this.ySpeed));

    }
}
