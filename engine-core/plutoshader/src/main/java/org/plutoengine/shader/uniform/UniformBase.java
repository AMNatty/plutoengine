package org.plutoengine.shader.uniform;

public abstract class UniformBase
{
    protected final int location;

    protected UniformBase(int location)
    {
        this.location = location;
    }

    public final int getLocation()
    {
        return this.location;
    }
}
