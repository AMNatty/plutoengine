package cz.tefek.pluto.lmdb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.MDBVal;

public abstract class LMDBKey
{
    public abstract MDBVal toMDBVal(MemoryStack stack);
}
