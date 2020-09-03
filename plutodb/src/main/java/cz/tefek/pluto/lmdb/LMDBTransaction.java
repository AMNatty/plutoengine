package cz.tefek.pluto.lmdb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.LMDB;

/**
 * @deprecated Usage discouraged until tested well enough.
 * */
@Deprecated
public class LMDBTransaction implements AutoCloseable
{
    private long ptr;
    private LMDBEnvironment env;
    private boolean readOnly;
    private boolean uncommited = true;

    LMDBTransaction(LMDBEnvironment env, long ptr, boolean readOnly)
    {
        this.env = env;
        this.ptr = ptr;
        this.readOnly = readOnly;
    }

    public long getAddress()
    {
        return this.ptr;
    }

    public LMDBEnvironment getEnvironment()
    {
        return this.env;
    }

    public boolean isReadOnly()
    {
        return this.readOnly;
    }

    private int getDatabase(String name)
    {
        try (var stack = MemoryStack.stackPush())
        {
            var intPtr = stack.mallocInt(1);

            int createFlag = this.readOnly ? 0 : LMDB.MDB_CREATE;

            int returnCode = LMDB.mdb_dbi_open(this.ptr, name, createFlag, intPtr);

            if (returnCode != LMDB.MDB_SUCCESS)
            {
                switch (returnCode)
                {
                    case LMDB.MDB_DBS_FULL:
                        throw new RuntimeException("Error: mdb_dbi_open failed with the following error: DBS_FULL (too many open databases)");

                    case LMDB.MDB_NOTFOUND:
                        throw new RuntimeException("Error: mdb_dbi_open failed with the following error: NOTFOUND (database was not found)");

                    default:
                        throw new RuntimeException(String.format("Error: mdb_dbi_open failed with the following error code: %d", returnCode));
                }
            }

            int dbHandle = intPtr.get(0);

            return dbHandle;
        }
    }

    public <K extends LMDBKey, V extends ILMDBValueRecipe> LMDBDatabase<K, V> getDatabase(LMDBSchema<K, V> schema)
    {
        var dbHandle = this.getDatabase(schema.getName());

        var recipeClazz = schema.getValueRecipe();

        try
        {
            return new LMDBDatabase<K, V>(this, dbHandle, recipeClazz);
        }
        catch (NoSuchMethodException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void commit()
    {
        int returnCode = LMDB.mdb_txn_commit(this.ptr);

        if (returnCode != LMDB.MDB_SUCCESS)
        {
            throw new RuntimeException(String.format("Error: mdb_txn_commit failed with the following error code: %d", returnCode));
        }

        this.uncommited = false;
    }

    public void abort()
    {
        LMDB.mdb_txn_abort(this.ptr);
        this.uncommited = false;
    }

    @Override
    public void close() throws Exception
    {
        if (this.readOnly || this.uncommited)
        {
            this.abort();
        }
    }
}
