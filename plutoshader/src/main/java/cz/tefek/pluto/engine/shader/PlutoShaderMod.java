package cz.tefek.pluto.engine.shader;

import cz.tefek.io.modloader.ModEntry;
import cz.tefek.pluto.engine.ModLWJGL;

@ModEntry(modid = PlutoShaderMod.MOD_ID, displayName = "PlutoShader", dependencies = { ModLWJGL.class }, version = "0.3", description = "Automated shader loader and manager.")
public class PlutoShaderMod
{
    public static final String MOD_ID = "plutoshader";

}
