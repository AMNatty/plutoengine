package cz.tefek.srclone.particle;

import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.math.BasicInterpolation;

import cz.tefek.srclone.SRCloneMod;
import cz.tefek.srclone.graphics.DirectionalSprite;

public class ParticleAnimatedSprite extends Particle
{
    protected DirectionalSprite sprite;

    public ParticleAnimatedSprite(DirectionalSprite sprite)
    {
        this.sprite = sprite;
    }

    @Override
    public void render()
    {
        var size = this.size * this.scale;

        RectangleRenderer2D.draw(SRCloneMod.centeredQuad)
                           .at(this.getRenderX(), this.getRenderY(), size, size)
                           .rotate(this.rotation)
                           .sprite(this.sprite.getSide(BasicInterpolation.floorLerp(this.animationTimer, 0, this.sprite.getSideCount() - 1)))
                           .flush();
    }
}
