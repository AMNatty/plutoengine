package org.plutoengine.collision;

public class CollisionClass
{
    private static int idSource = 0;

    private final int id = idSource++;
    private final boolean selfCollision;

    public CollisionClass(boolean selfCollision)
    {
        this.selfCollision = selfCollision;
    }

    public int getID()
    {
        return this.id;
    }

    public boolean hasSelfCollision()
    {
        return this.selfCollision;
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        CollisionClass that = (CollisionClass) o;
        return this.id == that.id;
    }
}
