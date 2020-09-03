package cz.tefek.pluto.lmdb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.MDBVal;

/**
 * @deprecated Usage discouraged until tested well enough.
 * */
@Deprecated
public abstract class LMDBKey
{
    public abstract MDBVal toMDBVal(MemoryStack stack);
}
