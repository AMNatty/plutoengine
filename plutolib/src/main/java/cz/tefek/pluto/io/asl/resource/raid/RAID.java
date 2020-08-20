package cz.tefek.pluto.io.asl.resource.raid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Runtime Assigned ID (or Resource Address ID)
 */
public class RAID<E extends IIdentifiable> implements Iterable<E>
{
    private static final int INITIAL_SIZE = 512;

    private List<E> raid;

    private Map<String, Integer> reverseRaid;

    public RAID()
    {
        this.raid = new ArrayList<>(INITIAL_SIZE);
        this.reverseRaid = new HashMap<>();
    }

    public void register(E item)
    {
        var address = item.getStringID();

        if (this.reverseRaid.containsKey(address))
        {
            throw new IllegalArgumentException("Cannot register two items with the same resource ID!");
        }

        var pos = this.raid.size();
        this.raid.add(item);
        this.reverseRaid.put(address, pos);
        item.onRegister(pos);
    }

    public E getByIntID(int id)
    {
        if (id < 0 || id >= this.raid.size())
        {
            return null;
        }

        return this.raid.get(id);
    }

    public int getIDOf(E item)
    {
        return this.reverseRaid.get(item.getStringID());
    }

    @Override
    public Iterator<E> iterator()
    {
        return this.raid.iterator();
    }
}
