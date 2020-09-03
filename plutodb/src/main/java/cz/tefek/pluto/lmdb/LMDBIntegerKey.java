package cz.tefek.pluto.lmdb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.MDBVal;

public class LMDBIntegerKey extends LMDBKey
{
    private final int key;

    private LMDBIntegerKey(int key)
    {
        this.key = key;
    }

    public static LMDBIntegerKey from(int rawKey)
    {
        return new LMDBIntegerKey(rawKey);
    }

    @Override
    public MDBVal toMDBVal(MemoryStack stack)
    {
        var keyBuf = stack.malloc(Integer.BYTES);

        keyBuf.putInt(this.key);

        return MDBVal.mallocStack(stack).mv_size(keyBuf.capacity()).mv_data(keyBuf);
    }
}
