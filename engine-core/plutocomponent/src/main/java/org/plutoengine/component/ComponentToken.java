package org.plutoengine.component;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public final class ComponentToken<T extends IComponent>
{
    private static final AtomicLong ID_SOURCE = new AtomicLong();

    private final long id;
    private final Supplier<T> supplier;

    private ComponentToken(Supplier<T> valueSupplier)
    {
        this.id = ID_SOURCE.getAndIncrement();
        this.supplier = valueSupplier;
    }

    public static <T extends IComponent> ComponentToken<T> create(@Nonnull Supplier<T> valueSupplier)
    {
        return new ComponentToken<>(valueSupplier);
    }

    public T createInstance()
    {
        return this.supplier.get();
    }

    public long getID()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ComponentToken<?> that = (ComponentToken<?>) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
    }
}
