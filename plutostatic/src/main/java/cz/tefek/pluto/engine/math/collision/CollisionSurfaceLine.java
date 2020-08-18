package cz.tefek.pluto.engine.math.collision;

import org.joml.LineSegmentf;

public class CollisionSurfaceLine extends CollisionSurface
{
    public CollisionSurfaceLine(float friction, float bounciness, float drag)
    {
        super(friction, bounciness, drag);
    }

    protected LineSegmentf lineSegment;

    public LineSegmentf getLineSegment()
    {
        return this.lineSegment;
    }
}
