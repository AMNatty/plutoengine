package cz.tefek.pluto.engine.math.collision;

public class CollisionClass
{
    private static int idSource = 0;

    private int id = idSource++;
    private boolean selfCollision;

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
}
