package org.plutoengine.graphics.sprite;

import org.plutoengine.display.Framerate;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import java.util.Arrays;

public class TemporalSprite implements Sprite<RectangleTexture>
{
    protected PartialTextureSprite[] frames;
    protected float[] durations;

    protected float cached = 0;
    protected int cacheIndex = 0;

    protected transient float[] stops;

    public TemporalSprite(int frameCount)
    {
        this.frames = new PartialTextureSprite[frameCount];
        this.durations = new float[frameCount];
        this.stops = new float[frameCount];
    }

    public TemporalSprite(PartialTextureSprite[] frames)
    {
        this.frames = frames;
        this.durations = new float[frames.length];
        this.stops = new float[frames.length];
    }

    public TemporalSprite(PartialTextureSprite[] frames, float duration)
    {
        this.frames = frames;
        this.durations = new float[frames.length];
        this.stops = new float[frames.length];

        Arrays.fill(this.durations, duration);
        this.recomputeFromIndex(0);
    }

    public TemporalSprite(PartialTextureSprite[] frames, float[] durations)
    {
        assert frames.length == durations.length;

        this.frames = frames;
        this.durations = durations;
        this.stops = new float[durations.length];

        this.recomputeFromIndex(0);
    }

    protected void recomputeFromIndex(int index)
    {
        for (int i = index + 1; i < this.durations.length; i++)
            this.stops[i] = this.stops[i - 1] + this.durations[i];
    }

    public void setFrame(int index, PartialTextureSprite sprite)
    {
        this.frames[index] = sprite;
        this.recomputeFromIndex(index);
    }

    public int getFrameCount()
    {
        return this.frames.length;
    }

    public PartialTextureSprite getFrameAt(float time)
    {
        if (time == this.cached)
            return this.frames[this.cacheIndex];

        var totalDuration = this.stops[this.stops.length - 1] + durations[this.durations.length - 1];
        time = (time % totalDuration + totalDuration) % totalDuration;
        var i = Math.abs(Arrays.binarySearch(this.stops, time));
        this.cacheIndex = i % this.durations.length;
        this.cached = time;
        return this.frames[this.cacheIndex];
    }

    public PartialTextureSprite getFrame()
    {
        return this.getFrameAt(Framerate.getAnimationTimer());
    }

    public PartialTextureSprite getFrameNr(int i)
    {
        return this.frames[i];
    }

    public PartialTextureSprite[] getFrames()
    {
        return this.frames;
    }

    @Override
    public int getX()
    {
        return this.getFrame().getX();
    }

    @Override
    public int getY()
    {
        return this.getFrame().getY();
    }

    @Override
    public int getWidth()
    {
        return this.getFrame().getWidth();
    }

    @Override
    public int getHeight()
    {
        return this.getFrame().getHeight();
    }

    @Override
    public RectangleTexture getSheet()
    {
        return this.getFrame().getSheet();
    }
}
