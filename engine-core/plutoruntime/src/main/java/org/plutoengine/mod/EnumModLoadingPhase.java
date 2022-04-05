package org.plutoengine.mod;

public enum EnumModLoadingPhase
{
    INITIAL,

    SCANNING_EXTERNAL,

    CLASSLOADING_EXTERNAL,

    LOADING,

    DONE,

    UNLOADING
}
