package cz.tefek.pluto.modloader.event;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.pluto.eventsystem.EventData;
import cz.tefek.pluto.modloader.ModEntry;

/**
 * Instances are passed to {@link ModEntry ModEntries} on post-load.
 *
 * @author 493msi
 */
public class ModPostLoadEvent extends EventData
{
    // TODO Cross-mod messaging
    public final List<String> crossMessages = new ArrayList<String>();
}
