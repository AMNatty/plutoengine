package cz.tefek.pluto.engine.graphics;

import cz.tefek.pluto.engine.graphics.font.FontManager;
import cz.tefek.pluto.engine.graphics.font.FontShader;
import cz.tefek.pluto.engine.graphics.texture.MagFilter;
import cz.tefek.pluto.engine.graphics.texture.MinFilter;
import cz.tefek.pluto.engine.graphics.texture.WrapMode;
import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;
import cz.tefek.pluto.engine.gui.font.FontRenderer;
import cz.tefek.pluto.engine.shader.RenderShaderBuilder;
import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.asl.resource.ResourceSubscriber;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.modloader.Mod;
import cz.tefek.pluto.modloader.ModEntry;
import cz.tefek.pluto.modloader.ModLoaderCore;
import cz.tefek.pluto.modloader.event.ModPreLoad;
import cz.tefek.pluto.modloader.event.ModPreLoadEvent;
import cz.tefek.pluto.modloader.event.ModUnload;
import cz.tefek.pluto.modloader.event.ModUnloadEvent;

/**
 * @author 493msi
 *
 */
@ModEntry(modid = PlutoGUIMod.MOD_ID, displayName = "Pluto Engine GUI Renderer", author = "Tefek", build = 2, dependencies = { PlutoSpriteSheetMod.class }, clientSupport = true, serverSupport = false, version = "0.2", description = "Everything GUI of PlutoEngine.")
public class PlutoGUIMod
{
    public static final String MOD_ID = "plutogui";

    public static Mod instance;
    public static ResourceSubscriber subscriber;

    public static RectangleTexture uiElementsAtlas;

    private static FontShader fontShader;

    @ModPreLoad
    public static void preLoad(ModPreLoadEvent event)
    {
        instance = ModLoaderCore.getModByID(MOD_ID);
        subscriber = instance.getDefaultResourceSubscriber();

        Logger.log("Intializing " + MOD_ID + "...");

        fontShader = new RenderShaderBuilder(subscriber, "shaders.VertexFontShader#glsl", "shaders.FragmentFontShader#glsl").build(FontShader.class, false);

        // Load the default font
        FontManager.loadFont(new ResourceAddress(subscriber, "font.default"));

        FontRenderer.load(fontShader);

        uiElementsAtlas = new RectangleTexture();
        uiElementsAtlas.load(new ResourceAddress(subscriber, "gui.elements#png"), MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
    }

    @ModUnload
    public static void unload(ModUnloadEvent unloadEvent)
    {
        uiElementsAtlas.delete();

        FontManager.unloadAll();

        FontRenderer.unload();
        fontShader.dispose();
    }
}
