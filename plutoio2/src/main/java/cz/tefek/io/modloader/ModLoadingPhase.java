package cz.tefek.io.modloader;

public enum ModLoadingPhase
{
    UPACKING,
    PREPARING,
    INITIALIZING,
    WAITING,
    PRELOADING,
    LOADING,
    POSTLOADING,
    FINISHING,
    CANCELED,
    INSTANTIATING,
    CLASSLOADING,
    UNLOADING;
}
