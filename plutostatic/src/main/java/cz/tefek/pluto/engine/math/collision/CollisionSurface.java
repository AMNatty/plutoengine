package cz.tefek.pluto.engine.math.collision;

public class CollisionSurface
{
    protected float friction; // 0..1 where 0 is no friction and 1 is superglue
    protected float bounciness; // 0..n where 0 is no bounciness and 1 is is a perfect bounce
    protected float drag; // 0..n where the value defines how much the surface is affected by air 

    public CollisionSurface(float friction, float bounciness, float drag)
    {
        this.friction = friction;
        this.bounciness = bounciness;
        this.drag = drag;
    }

    /**
     * Returns the friction; a value 0..1 where 0 is no friction and 1 is infinite
     * friction, making the surfaces impossible to slide on.
     * 
     * @return the friction value
     */
    public float getFriction()
    {
        return this.friction;
    }

    /**
     * Sets the friction; a value 0..1 where 0 is no friction and 1 is infinite
     * friction, making the surfaces impossible to slide on.
     * 
     * @param friction the friction value
     */
    public void setFriction(float friction)
    {
        this.friction = friction;
    }

    /**
     * Returns the bounciness; a value 0..1..n where 0 means the surface doesn't
     * bounce off at all and 1 means the object bounces off at the exact same
     * velocity, values above 1 might have some undesirable effects and are
     * discouraged.
     * 
     * @return the bounciness
     */
    public float getBounciness()
    {
        return this.bounciness;
    }

    /**
     * Sets the bounciness; a value 0..1..n where 0 means the surface doesn't bounce
     * off at all and 1 means the object bounces off at the exact same velocity,
     * values above 1 might have some undesirable effects and are discouraged.
     * 
     * @param bounciness the bounciness value
     */
    public void setBounciness(float bounciness)
    {
        this.bounciness = bounciness;
    }

    /**
     * @return the drag
     */
    public float getDrag()
    {
        return this.drag;
    }

    /**
     * Sets the drag modifier; a value 0..n which defines how much this surface is
     * affected by air resistance. A value of 1 should be the golden standard for
     * most objects.
     * 
     * @param drag the drag modifier
     */
    public void setDrag(float drag)
    {
        this.drag = drag;
    }

}
