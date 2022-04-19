package org.plutoengine;

import org.lwjgl.glfw.GLFW;

import org.plutoengine.mod.ModEntry;

@ModEntry(modID = "glfw", version = ModGLFW.VERSION, dependencies = ModLWJGL.class)
public class ModGLFW
{
    public static final String VERSION = GLFW.GLFW_VERSION_MAJOR + "." + GLFW.GLFW_VERSION_MINOR + "." + GLFW.GLFW_VERSION_REVISION;
}
