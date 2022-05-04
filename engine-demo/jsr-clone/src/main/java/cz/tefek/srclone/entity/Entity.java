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

package cz.tefek.srclone.entity;

import org.joml.Vector3f;
import org.plutoengine.audio.RandomAccessClip;
import org.plutoengine.audio.al.SoundEffect;

import cz.tefek.srclone.EnumTeam;
import cz.tefek.srclone.Game;
import cz.tefek.srclone.IGameObject;

public abstract class Entity implements IGameObject
{
    protected float x;
    protected float y;

    protected boolean collision;

    protected float size;

    protected float rotation;

    protected float lifetime;

    protected boolean deadFlag;

    protected EnumTeam team;

    protected Game game;

    protected Entity()
    {
        this.collision = true;
    }

    @Override
    public void init(Game game, float x, float y)
    {
        this.game = game;
        this.x = x;
        this.y = y;
    }

    public void render()
    {

    }

    public void tick(float delta)
    {
        this.lifetime += delta;
    }

    public boolean isDead()
    {
        return this.deadFlag;
    }

    protected final float getRenderX()
    {
        return this.x + this.game.getViewX();
    }

    protected final float getRenderY()
    {
        return this.y + this.game.getViewY();
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getX()
    {
        return this.x;
    }

    public float getY()
    {
        return this.y;
    }

    public EnumTeam getTeam()
    {
        return this.team;
    }

    public float getDistance(Entity entity)
    {
        return (float) (Math.hypot(entity.x - this.x, entity.y - this.y) - this.size - entity.size);
    }

    public boolean collides(Entity entity)
    {
        float dx = entity.x - this.x;
        float dy = entity.y - this.y;

        return dx * dx + dy * dy < this.size * this.size + entity.size * entity.size;
    }

    protected void emitSoundEffect(RandomAccessClip clip, float volume, float pitch, boolean closeOnDeath)
    {
        var srae = this.game.getAudioEngine();
        var audioEngine = srae.getAudioEngine();
        var sfx = new SoundEffect(clip, new Vector3f(this.x, this.y, 0.0f), volume)
            .pitch(pitch)
            .movementMapper(oldPos -> this.deadFlag ? oldPos : new Vector3f(this.x, this.y, 0.0f));

        if (closeOnDeath)
            sfx.keepAliveFunction(() -> !this.deadFlag);

        audioEngine.playSound(sfx);
    }

    public boolean hasCollision()
    {
        return this.collision;
    }
}
