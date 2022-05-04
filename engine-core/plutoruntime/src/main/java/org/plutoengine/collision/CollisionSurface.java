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

package org.plutoengine.collision;

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
