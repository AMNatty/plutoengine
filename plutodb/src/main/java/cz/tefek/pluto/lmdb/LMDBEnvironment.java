package cz.tefek.pluto.lmdb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.LMDB;

import java.io.File;
import java.io.IOException;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class LMDBEnvironment implements AutoCloseable
{
    private static final Set<File> envGuard = Collections.synchronizedSet(new HashSet<>());

    private final File path;
    private final long envPtr;
    private final int maxDBs;
    private static final int permissions = 0640;

    public LMDBEnvironment(File path, int maxDBs, long mapSize) throws IOException
    {
        if (!envGuard.add(path))
        {
            throw new RuntimeException("Error: The LMDB Environment guard has detected a creation of an environment on an already locked file. To avoid synchronization issues, this cannot be permitted.");
        }

        this.path = path;
        this.maxDBs = maxDBs;

        try (var stack = MemoryStack.stackPush())
        {
            var ptrBuf = stack.mallocPointer(1);
            int createCode = LMDB.mdb_env_create(ptrBuf);

            if (createCode != LMDB.MDB_SUCCESS)
            {
                throw new RuntimeException(String.format("LMDB environment failed to create! Error code: %d", createCode));
            }

            this.envPtr = ptrBuf.get(0);
        }

        int mapSizeResult = LMDB.mdb_env_set_mapsize(this.envPtr, mapSize);

        if (mapSizeResult != LMDB.MDB_SUCCESS)
        {
            throw new RuntimeException(String.format("Failed to set the MDB map size! Error code: %d", mapSizeResult));
        }

        int maxDBsResult = LMDB.mdb_env_set_maxdbs(this.envPtr, this.maxDBs);

        if (maxDBsResult != LMDB.MDB_SUCCESS)
        {
            throw new RuntimeException(String.format("Failed to set the MDB maximum database count! Error code: %d", maxDBsResult));
        }

        if (!path.isDirectory())
        {
            path.mkdirs();
        }

        int openCode = LMDB.mdb_env_open(this.envPtr, path.getAbsolutePath(), 0, permissions);

        if (openCode != LMDB.MDB_SUCCESS)
        {
            throw new RuntimeException(String.format("LMDB environment failed to open! Error code: %d", openCode));
        }
    }

    public LMDBTransaction createTransaction()
    {
        return this.createTransaction(false);
    }

    public synchronized LMDBTransaction createTransaction(boolean readOnly)
    {
        try (var stack = MemoryStack.stackPush())
        {
            var ptrBuf = stack.mallocPointer(1);

            int code = LMDB.mdb_txn_begin(this.envPtr, 0, readOnly ? LMDB.MDB_RDONLY : 0, ptrBuf);

            if (code != LMDB.MDB_SUCCESS)
            {
                String errStr;

                switch (code)
                {
                    case LMDB.MDB_PANIC:
                        errStr = "MDB_PANIC";
                        break;

                    case LMDB.MDB_MAP_RESIZED:
                        errStr = "MDB_MAP_RESIZED";
                        break;

                    case LMDB.MDB_READERS_FULL:
                        errStr = "MDB_READERS_FULL";
                        break;

                    default:
                        throw new RuntimeException(String.format("Failed to create a transaction. Error code: %d", code));
                }

                throw new RuntimeException(String.format("Failed to create a transaction. Error code: %s", errStr));
            }

            long ptr = ptrBuf.get(0);

            return new LMDBTransaction(this, ptr, readOnly);
        }
    }

    public long getAddress()
    {
        return this.envPtr;
    }

    @Override
    public void close() throws Exception
    {
        LMDB.mdb_env_close(this.envPtr);
        envGuard.remove(this.path);
    }
}
