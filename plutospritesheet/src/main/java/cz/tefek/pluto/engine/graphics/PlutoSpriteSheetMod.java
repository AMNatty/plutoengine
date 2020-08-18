package cz.tefek.pluto.engine.graphics;

import cz.tefek.io.asl.resource.ResourceSubscriber;
import cz.tefek.io.modloader.Mod;
import cz.tefek.io.modloader.ModEntry;
import cz.tefek.io.modloader.ModLoaderCore;
import cz.tefek.io.modloader.event.ModPreLoad;
import cz.tefek.io.modloader.event.ModPreLoadEvent;
import cz.tefek.io.modloader.event.ModUnload;
import cz.tefek.io.modloader.event.ModUnloadEvent;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.pluto.engine.ModLWJGL;
import cz.tefek.pluto.engine.graphics.spritesheet.FramebufferTiledSpriteSheet;
import cz.tefek.pluto.engine.shader.PlutoShaderMod;
import cz.tefek.pluto.engine.shader.RenderShaderBuilder;

@ModEntry(modid = PlutoSpriteSheetMod.MOD_ID, version = "0.2", dependencies = { ModLWJGL.class, PlutoShaderMod.class }, author = "493msi", build = 1, displayName = "Pluto SpriteSheet", description = "A library to manage, store and draw sprites.")
public class PlutoSpriteSheetMod
{
    public static final String MOD_ID = "plutospritesheet";

    public static Mod instance;
    public static ResourceSubscriber subscriber;

    /**
     * Strictly internal use only, do NOT use this outside of plutospritesheet
     */
    private static Shader2D shader2D;

    /**
     * Strictly internal use only, do NOT use this outside of plutospritesheet
     */
    private static ShaderRectangle2D shaderRectangle2D;

    /**
     * Strictly internal use only, do NOT use this outside of plutospritesheet
     */
    private static ShaderRectangle2D spriteSheetShader;

    @ModPreLoad
    public static void preLoad(ModPreLoadEvent event)
    {
        instance = ModLoaderCore.getModByID(MOD_ID);
        subscriber = instance.getDefaultResourceSubscriber();

        Logger.log("Intializing " + MOD_ID + "...");

        shader2D = new RenderShaderBuilder(subscriber, "shaders.v2D#glsl", "shaders.f2D#glsl").build(Shader2D.class, false);
        shaderRectangle2D = new RenderShaderBuilder(subscriber, "shaders.VertexRectangle2D#glsl", "shaders.FragmentRectangle2D#glsl").build(ShaderRectangle2D.class, false);
        spriteSheetShader = new RenderShaderBuilder(subscriber, "shaders.VertexSpriteSheet#glsl", "shaders.FragmentSpriteSheet#glsl").build(ShaderRectangle2D.class, false);

        Renderer2D.load(shader2D);
        RectangleRenderer2D.load(shaderRectangle2D);

        FramebufferTiledSpriteSheet.setSpriteShader(spriteSheetShader);
    }

    @ModUnload
    public static void unload(ModUnloadEvent unloadEvent)
    {
        FramebufferTiledSpriteSheet.setSpriteShader(null);

        spriteSheetShader.dispose();
        shaderRectangle2D.dispose();
        shader2D.dispose();

        RectangleRenderer2D.unload();
        Renderer2D.unload();
    }
}
