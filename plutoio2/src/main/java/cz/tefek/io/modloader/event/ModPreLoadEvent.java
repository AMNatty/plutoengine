package cz.tefek.io.modloader.event;

import java.util.List;

import cz.tefek.io.modloader.Mod;
import cz.tefek.io.modloader.ModEntry;
import cz.tefek.pluto.eventsystem.EventData;

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
