package cz.tefek.pluto.engine;

import org.lwjgl.Version;

import cz.tefek.pluto.modloader.ModEntry;

@ModEntry(modid = "modlwjgl", version = ModLWJGL.version, author = "The LWJGL team", displayName = "LWJGL", description = "The LWJGL library, without which the Pluto Engine wouldn't exist.")
public class ModLWJGL
{
    public static final String version = Version.VERSION_MAJOR + "." + Version.VERSION_MINOR + "." + Version.VERSION_REVISION;
}
