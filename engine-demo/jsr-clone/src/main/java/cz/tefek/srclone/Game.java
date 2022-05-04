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

package cz.tefek.srclone;

import org.lwjgl.glfw.GLFW;
import org.plutoengine.Pluto;
import org.plutoengine.PlutoLocal;
import org.plutoengine.display.Display;
import org.plutoengine.graphics.ImmediateFontRenderer;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.logger.Logger;
import org.plutoengine.util.color.Color;

import java.util.*;

import cz.tefek.srclone.ammo.EnumAmmo;
import cz.tefek.srclone.audio.SRAudioEngine;
import cz.tefek.srclone.entity.Entity;
import cz.tefek.srclone.entity.EntityPlayer;
import cz.tefek.srclone.entity.projectile.EntityProjectile;
import cz.tefek.srclone.particle.Particle;

public class Game
{
    private float camX;
    private float camY;

    private float viewX;
    private float viewY;

    private float deathScreenAnimation;

    private final List<Entity> entities;
    private final List<EntityProjectile> projectiles;
    private final List<Particle> particles;

    private final EntityPlayer entityPlayer;

    private final List<Entity> newEntities;
    private final List<EntityProjectile> newProjectiles;
    private final List<Particle> newParticles;

    private final Random random;

    private final SRAudioEngine audioEngine;

    private final GameDirector director;

    public Game()
    {
        Logger.log("==== NEW GAME ====");

        this.camX = 0;
        this.camY = 0;

        this.entities = new ArrayList<>();
        this.newEntities = new ArrayList<>();

        this.projectiles = new ArrayList<>();
        this.newProjectiles = new ArrayList<>();

        this.entityPlayer = new EntityPlayer();
        this.entityPlayer.init(this, 0.0f, 0.0f);
        this.entityPlayer.addAmmo(EnumAmmo.P_LASER_BEAM, EnumAmmo.AMMO_INFINITE);

        // DEBUG

        if (Pluto.DEBUG_MODE)
        {
            Arrays.stream(EnumAmmo.values()).forEach(a -> this.entityPlayer.addAmmo(a, EnumAmmo.AMMO_INFINITE));
        }

        this.particles = new ArrayList<>();
        this.newParticles = new ArrayList<>();

        this.random = new Random();

        this.audioEngine = new SRAudioEngine();

        this.director = new GameDirector(this);
    }

    public void tick(double frameTime)
    {
        if (frameTime != 0 && !Double.isNaN(frameTime))
        {
            var ft = (float) frameTime;

            if (!this.isOver())
            {
                this.entities.forEach(e -> e.tick(ft));
                this.projectiles.forEach(p -> p.tick(ft));
                this.entityPlayer.tick(ft);

                this.director.tick(frameTime);

                this.entities.removeIf(Entity::isDead);
                this.entities.addAll(this.newEntities);
                this.newEntities.clear();

                this.projectiles.removeIf(EntityProjectile::isDead);
                this.projectiles.addAll(this.newProjectiles);
                this.newProjectiles.clear();
            }

            this.particles.forEach(p -> p.tick(ft));
            this.particles.removeIf(Particle::isDead);
            this.particles.addAll(this.newParticles);
            this.newParticles.clear();
        }

        if (this.isOver())
        {
            this.deathScreenAnimation += frameTime;
            this.audioEngine.stopMusic(this);
        }

        this.audioEngine.tick(this);

        this.render();
    }

    private void render()
    {
        this.camX = -this.entityPlayer.getX();
        this.camY = -this.entityPlayer.getY();

        var display = PlutoLocal.components().getComponent(Display.class);
        this.viewX = this.camX + display.getWidth() / 2.0f;
        this.viewY = this.camY + display.getHeight() / 2.0f;

        SRCloneMod.starField.render(this.viewX, this.viewY);

        this.entities.forEach(Entity::render);

        if (!this.isOver())
            this.entityPlayer.render();

        this.projectiles.forEach(Entity::render);

        this.particles.forEach(Particle::render);

        var selectedAmmo = this.entityPlayer.getSelectedAmmo();
        long ammoCnt = this.entityPlayer.getAmmo(selectedAmmo);

        String ammoStr;

        if (ammoCnt == EnumAmmo.AMMO_INFINITE)
            ammoStr = "%sx∞";
        else
            ammoStr = "%%sx%06d".formatted(ammoCnt);

        var ammoFont = new TextStyleOptions(24)
            .setVerticalAlign(TextStyleOptions.TextAlign.START)
            .setPaint(LiPaint.solidColor(Color.WHITE));

        ImmediateFontRenderer.drawString(5.0f, display.getHeight() - 58.0f, ammoStr.formatted(selectedAmmo.getTextIcon()), SRCloneMod.srCloneFont, ammoFont);

        var healthFont = new TextStyleOptions(48)
            .setVerticalAlign(TextStyleOptions.TextAlign.START)
            .setPaint(LiPaint.solidColor(Color.WHITE));
        ImmediateFontRenderer.drawString(5.0f, display.getHeight() - 5.0f, "\uD83D\uDC96%03.0f".formatted(this.entityPlayer.getHealth()), SRCloneMod.srCloneFont, healthFont);

        var scoreFont = new TextStyleOptions(24)
            .setPaint(LiPaint.solidColor(Color.WHITE));
        ImmediateFontRenderer.drawString(5.0f, 5.0f, ("S %010.0f").formatted(this.entityPlayer.getScore()), SRCloneMod.srCloneFont, scoreFont);

        if (this.deathScreenAnimation > 1.0f)
        {
            var youDiedStyle = new TextStyleOptions(64)
                .setHorizontalAlign(TextStyleOptions.TextAlign.CENTER)
                .setVerticalAlign(TextStyleOptions.TextAlign.START)
                .setPaint(LiPaint.solidColor(Color.CRIMSON));
            ImmediateFontRenderer.drawString(display.getWidth() / 2.0f, display.getHeight() / 2.0f - 6.0f, "You Died!", SRCloneMod.font, youDiedStyle);
            youDiedStyle.setPaint(LiPaint.solidColor(Color.WHITE));
            ImmediateFontRenderer.drawString(display.getWidth() / 2.0f, display.getHeight() / 2.0f - 8.0f, "You Died!", SRCloneMod.font, youDiedStyle);

            var playAgainKey = String.valueOf(GLFW.glfwGetKeyName(GLFW.GLFW_KEY_R, GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_R)));
            var playAgainStr = "Press [%s] to play again...".formatted(playAgainKey.toUpperCase(Locale.ROOT));
            var playAgainStyle = new TextStyleOptions(40)
                .setHorizontalAlign(TextStyleOptions.TextAlign.CENTER)
                .setVerticalAlign(TextStyleOptions.TextAlign.END)
                .setPaint(LiPaint.solidColor(Color.CRIMSON));
            ImmediateFontRenderer.drawString(display.getWidth() / 2.0f, display.getHeight() / 2.0f + 20.0f, playAgainStr, SRCloneMod.font, playAgainStyle);
            playAgainStyle.setPaint(LiPaint.solidColor(Color.WHITE));
            ImmediateFontRenderer.drawString(display.getWidth() / 2.0f, display.getHeight() / 2.0f + 18.0f, playAgainStr, SRCloneMod.font, playAgainStyle);
        }
    }

    public SRAudioEngine getAudioEngine()
    {
        return this.audioEngine;
    }

    public void addEntity(Entity entity, float x, float y)
    {
        if (entity instanceof EntityProjectile projectile)
        {
            this.addProjectile(projectile, x, y);
            return;
        }

        this.newEntities.add(entity);
        entity.init(this, x, y);
    }

    public void addParticle(Particle particle, float x, float y)
    {
        this.newParticles.add(particle);
        particle.init(this, x, y);
    }

    private void addProjectile(EntityProjectile projectile, float x, float y)
    {
        this.newProjectiles.add(projectile);
        projectile.init(this, x, y);
    }

    public boolean isOver()
    {
        return this.entityPlayer.isDead();
    }

    public float getDeathScreenAnimation()
    {
        return this.deathScreenAnimation;
    }

    public List<Entity> getEntities()
    {
        return this.entities;
    }

    public EntityPlayer getEntityPlayer()
    {
        return this.entityPlayer;
    }

    public Random getRandom()
    {
        return this.random;
    }

    public float getViewX()
    {
        return this.viewX;
    }

    public float getViewY()
    {
        return this.viewY;
    }
}
