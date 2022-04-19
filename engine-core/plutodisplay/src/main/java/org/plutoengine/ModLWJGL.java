package org.plutoengine;

import org.lwjgl.Version;

import org.plutoengine.mod.ModEntry;

@ModEntry(modID = "lwjgl",
        version = ModLWJGL.version)
public class ModLWJGL
{
    public static final String version = Version.VERSION_MAJOR + "." + Version.VERSION_MINOR + "." + Version.VERSION_REVISION;
}
