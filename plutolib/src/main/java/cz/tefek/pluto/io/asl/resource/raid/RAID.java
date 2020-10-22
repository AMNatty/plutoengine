package cz.tefek.pluto.io.asl.resource.raid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Runtime Assigned ID (or Resource Address ID)
 */
public class RAID<E extends IIdentifiable> implements Iterable<E>
{
    private static final int INITIAL_SIZE = 512;

    private final List<E> raid;

    private final Map<String, Integer> reverseRaid;

    public RAID()
    {
        this.raid = new ArrayList<>(INITIAL_SIZE);
        this.reverseRaid = new HashMap<>();
    }

    public void register(@Nonnull E item)
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

    @Nullable
    public E getByIntID(int id)
    {
        if (id < 0 || id >= this.raid.size())
        {
            return null;
        }

        return this.raid.get(id);
    }

    @Nonnull
    public OptionalInt getIDOf(@Nonnull E item)
    {
        return reverseRaid.containsKey(item.getStringID()) ?
            OptionalInt.of(reverseRaid.get(item.getStringID()))
                :
            OptionalInt.empty();
    }

    @Nonnull
    @Override
    public Iterator<E> iterator()
    {
        return this.raid.iterator();
    }
}
