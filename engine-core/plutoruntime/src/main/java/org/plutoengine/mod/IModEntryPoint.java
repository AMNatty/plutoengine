package org.plutoengine.mod;

/**
 * The main entry point for non-virtual mods.
 *
 * @author 493msi
 * @since 20.2.0.0-alpha.3
 * */
public interface IModEntryPoint
{
    /**
     * Called when the mod loader loads this mod.
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     * @param mod The {@link Mod} object
     * */
    default void onLoad(Mod mod)
    {

    }

    /**
     * Called when the mod loader unloads this mod.
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     * */
    default void onUnload()
    {

    }
}
