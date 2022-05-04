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

package org.plutoengine.gameobject;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameObjectRegistry<H, E extends IGameObject<H>> implements Iterable<E>
{
    private static final int INITIAL_SIZE = 512;

    private final List<E> lookup;
    private final Map<H, E> reverseLookup;

    public GameObjectRegistry()
    {
        this.lookup = new ArrayList<>(INITIAL_SIZE);
        this.reverseLookup = new HashMap<>(INITIAL_SIZE);
    }

    public void register(E item)
    {
        var key = item.objectKey();

        if (this.reverseLookup.containsKey(key))
            throw new IllegalArgumentException("Cannot register two items with the same key!");

        var position = this.lookup.size();
        this.lookup.add(item);
        this.reverseLookup.put(key, item);
        item.onRegister(position);
    }

    public Optional<E> getByID(int id)
    {
        try
        {
            return Optional.of(this.lookup.get(id));
        }
        catch (IndexOutOfBoundsException e)
        {
            return Optional.empty();
        }
    }

    public Optional<E> getByKey(H key)
    {
        return Optional.of(this.reverseLookup.get(key));
    }

    public Optional<Integer> getIDOf(H key)
    {
        return this.getByKey(key).map(IGameObject::objectID);
    }

    public Optional<H> getKeyOf(int id)
    {
        return this.getByID(id).map(IGameObject::objectKey);
    }

    @NotNull
    @Override
    public Iterator<E> iterator()
    {
        return this.lookup.iterator();
    }
}
