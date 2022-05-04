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

package org.plutoengine.component;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public final class ComponentToken<T extends AbstractComponent<? super T>>
{
    private static final AtomicLong ID_SOURCE = new AtomicLong();

    private final long id;
    private final Supplier<T> supplier;

    private ComponentToken(Supplier<T> valueSupplier)
    {
        this.id = ID_SOURCE.getAndIncrement();
        this.supplier = valueSupplier;
    }

    public static <G extends AbstractComponent<? super G>> ComponentToken<G> create(@NotNull Supplier<G> valueSupplier)
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
