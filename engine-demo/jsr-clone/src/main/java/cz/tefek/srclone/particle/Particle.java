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

package cz.tefek.srclone.particle;

import cz.tefek.srclone.Game;

public abstract class Particle
{
    protected float x;
    protected float y;

    protected float size;

    protected float rotation;
    protected float rotationalVelocity;
    protected float maxRotationalVelocity;
    protected float rotationVariation;

    protected float lifetime;
    protected float initialLifetime;
    protected float lifetimeVariation;

    protected float initialScale;
    protected float finalScale;
    protected float scale;

    protected float animationTimer;

    protected boolean deadFlag;

    protected Game game;

    public Particle()
    {
        this.initialScale = 1.0f;
        this.finalScale = 1.0f;
    }

    public void init(Game game, float x, float y)
    {
        this.game = game;

        var rand = this.game.getRandom();

        this.x = x;
        this.y = y;
        this.scale = this.initialScale;
        this.lifetime = this.initialLifetime = this.initialLifetime + (rand.nextFloat() * 2.0f - 1.0f) * this.lifetimeVariation;
        this.rotation = this.rotation + (rand.nextFloat() * 2.0f - 1.0f) * this.rotationVariation;
        this.rotationalVelocity = this.rotationalVelocity + (rand.nextFloat() * 2.0f - 1.0f) * this.maxRotationalVelocity;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public void setSize(float size)
    {
        this.size = size;
    }

    protected final float getRenderX()
    {
        return this.x + this.game.getViewX();
    }

    protected final float getRenderY()
    {
        return this.y + this.game.getViewY();
    }

    public void tick(float frameTime)
    {
        this.rotation += this.rotationalVelocity * frameTime;
        this.lifetime -= frameTime;

        if (this.lifetime <= 0)
        {
            this.deadFlag = true;
            return;
        }

        float particleProgress = 1 - this.lifetime / this.initialLifetime;

        if (particleProgress >= 0 && particleProgress <= 1)
        {
            this.animationTimer = particleProgress;
        }

        this.scale = this.initialScale * (1 - this.animationTimer) + this.finalScale * this.animationTimer;
    }

    public abstract void render();

    public boolean isDead()
    {
        return this.deadFlag;
    }
}
