package org.plutoengine.component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractComponent implements IComponent
{
    private static final AtomicLong ID_SOURCE = new AtomicLong();

    private final long id;

    protected AbstractComponent()
    {
        this.id = ID_SOURCE.getAndIncrement();
    }

    @Override
    public long getID()
    {
        return this.id;
    }

    @Override
    public void onMount() throws Exception
    {

    }

    @Override
    public void onUnmount() throws Exception
    {

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        AbstractComponent that = (AbstractComponent) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
    }
}
