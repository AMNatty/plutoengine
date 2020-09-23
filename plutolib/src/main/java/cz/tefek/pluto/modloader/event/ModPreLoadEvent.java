package cz.tefek.pluto.modloader.event;

import java.util.List;

import cz.tefek.pluto.event.EventData;
import cz.tefek.pluto.modloader.Mod;
import cz.tefek.pluto.modloader.ModEntry;

/**
 * Instances are passed to {@link ModEntry ModEntries} on load. Carries a list
 * of all {@link Mod} objects.
 *
 * @author 493msi
 *
 */
public class ModPreLoadEvent extends EventData
{
    public List<Mod> mods;
}
