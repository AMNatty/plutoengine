package cz.tefek.plutodb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.MDBVal;

public class LMDBLongKey extends LMDBKey
{
    private long key;

    private LMDBLongKey(long key)
    {
        this.key = key;
    }

    public static LMDBLongKey from(long rawKey)
    {
        return new LMDBLongKey(rawKey);
    }

    @Override
    public MDBVal toMDBVal(MemoryStack stack)
    {
        var keyBuf = stack.malloc(Long.BYTES);

        keyBuf.putLong(this.key);

        return MDBVal.mallocStack(stack).mv_size(keyBuf.capacity()).mv_data(keyBuf);
    }
}
