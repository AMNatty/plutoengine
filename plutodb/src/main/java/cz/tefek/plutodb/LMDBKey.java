package cz.tefek.plutodb;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.lmdb.MDBVal;

public abstract class LMDBKey
{
    public abstract MDBVal toMDBVal(MemoryStack stack);
}
