package cz.tefek.pluto.lmdb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.LMDB;
import org.lwjgl.util.lmdb.MDBVal;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class LMDBDatabase<K extends LMDBKey, V extends ILMDBValueRecipe>
{
    private LMDBTransaction transaction;
    private int handle;
    private MethodHandle valueConstructor;

    LMDBDatabase(LMDBTransaction transaction, int handle, Class<V> valueClass) throws NoSuchMethodException, IllegalAccessException
    {
        this.handle = handle;
        this.transaction = transaction;
        this.valueConstructor = MethodHandles.lookup().findConstructor(valueClass, MethodType.methodType(void.class));
    }

    public long getHandle()
    {
        return this.handle;
    }

    public void put(K key, V value)
    {
        try (var stack = MemoryStack.stackPush())
        {
            var keyStruct = key.toMDBVal(stack);

            var size = value.sizeOf();

            var data = stack.malloc(size);

            value.serialize(data);

            data.flip();

            var valueStruct = MDBVal.mallocStack(stack).mv_data(data).mv_size(size);

            assert LMDB.mdb_put(this.transaction.getAddress(), this.handle, keyStruct, valueStruct, 0) == LMDB.MDB_SUCCESS;
        }
    }

    public V get(K key)
    {
        try
        {
            var valueToUpdate = (V) this.valueConstructor.invoke();
            return this.get(key, valueToUpdate);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public V get(K key, V valueToUpdate)
    {
        try (var stack = MemoryStack.stackPush())
        {
            var keyStruct = key.toMDBVal(stack);

            var valueStruct = MDBVal.mallocStack(stack);

            assert LMDB.mdb_get(this.transaction.getAddress(), this.handle, keyStruct, valueStruct) == LMDB.MDB_SUCCESS;

            valueToUpdate.deserialize(valueStruct.mv_data());

            return valueToUpdate;
        }
    }
}
