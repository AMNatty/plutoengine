package cz.tefek.pluto.engine.shader;

import cz.tefek.pluto.Pluto;
import cz.tefek.pluto.engine.ModLWJGL;
import cz.tefek.pluto.modloader.ModEntry;

@ModEntry(modid = PlutoShaderMod.MOD_ID,
        displayName = "PlutoShader",
        dependencies = { ModLWJGL.class },
        version = Pluto.VERSION,
        description = "Automated shader loader and manager.")
public class PlutoShaderMod
{
    public static final String MOD_ID = "plutoshader";

}
