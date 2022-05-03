package org.plutoengine.graphics;

import org.plutoengine.ModLWJGL;
import org.plutoengine.Pluto;
import org.plutoengine.mod.ModEntry;

@ModEntry(modID = PlutoRendererMod.MOD_ID,
        dependencies = { ModLWJGL.class },
        version = Pluto.VERSION)
public class PlutoRendererMod
{
    public static final String MOD_ID = "tefek.plutorender";

}
