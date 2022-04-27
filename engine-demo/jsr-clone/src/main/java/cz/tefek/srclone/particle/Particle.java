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
