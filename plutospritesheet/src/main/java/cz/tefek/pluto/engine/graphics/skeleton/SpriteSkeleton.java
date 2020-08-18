package cz.tefek.pluto.engine.graphics.skeleton;

import org.joml.Vector2f;
import org.joml.Vector2fc;

public class SpriteSkeleton
{
    protected SpriteSkeletonLimb rootLimb;
    protected static final Vector2f DEFAULT_SCALE = new Vector2f(1);
    protected static final Vector2f DEFAULT_SCALE_FLIPPED = new Vector2f(-1, 1);

    public void render(Vector2fc position, Vector2fc scale)
    {
        this.rootLimb.render(position, scale);
    }

    public void render(Vector2fc position)
    {
        this.render(position, DEFAULT_SCALE);
    }

    public SpriteSkeletonLimb getRootLimb()
    {
        return this.rootLimb;
    }
}
