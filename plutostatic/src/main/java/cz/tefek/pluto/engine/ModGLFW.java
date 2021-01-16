package cz.tefek.pluto.engine;

import org.lwjgl.glfw.GLFW;

import cz.tefek.pluto.modloader.ModEntry;

@ModEntry(modid = "modglfw",
    version = ModGLFW.VERSION,
    author = "The GLFW team",
    displayName = "GLFW",
    description = "The GLFW library, used for native window creation.",
    dependencies = ModLWJGL.class)
public class ModGLFW
{
    public static final String VERSION = GLFW.GLFW_VERSION_MAJOR + "." + GLFW.GLFW_VERSION_MINOR + "." + GLFW.GLFW_VERSION_REVISION;
}
