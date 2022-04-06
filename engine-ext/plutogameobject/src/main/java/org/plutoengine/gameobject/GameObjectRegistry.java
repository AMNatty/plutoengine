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
