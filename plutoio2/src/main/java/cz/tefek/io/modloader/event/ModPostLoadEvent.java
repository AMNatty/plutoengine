package cz.tefek.io.modloader.event;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.io.modloader.ModEntry;
import cz.tefek.pluto.eventsystem.EventData;

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
