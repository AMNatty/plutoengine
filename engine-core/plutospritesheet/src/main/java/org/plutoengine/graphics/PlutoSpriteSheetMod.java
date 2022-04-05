package org.plutoengine.graphics;

import org.plutoengine.ModLWJGL;
import org.plutoengine.Pluto;
import org.plutoengine.graphics.spritesheet.FramebufferTiledSpriteSheet;
import org.plutoengine.mod.IModEntryPoint;
import org.plutoengine.mod.Mod;
import org.plutoengine.mod.ModEntry;
import org.plutoengine.shader.PlutoShaderMod;
import org.plutoengine.shader.RenderShaderBuilder;

@ModEntry(modID = PlutoSpriteSheetMod.MOD_ID,
        version = Pluto.VERSION,
        dependencies = { ModLWJGL.class, PlutoShaderMod.class })
public class PlutoSpriteSheetMod implements IModEntryPoint
{
    public static final String MOD_ID = "tefek.plutospritesheet";

    public static Mod instance;

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

    @Override
    public void onLoad(Mod mod)
    {
        instance = mod;

        shader2D = new RenderShaderBuilder(mod.getResource("shaders.v2D#glsl"), mod.getResource("shaders.f2D#glsl")).build(Shader2D.class, false);
        shaderRectangle2D = new RenderShaderBuilder(mod.getResource("shaders.VertexRectangle2D#glsl"), mod.getResource("shaders.FragmentRectangle2D#glsl")).build(ShaderRectangle2D.class, false);
        spriteSheetShader = new RenderShaderBuilder(mod.getResource("shaders.VertexSpriteSheet#glsl"), mod.getResource("shaders.FragmentSpriteSheet#glsl")).build(ShaderRectangle2D.class, false);

        Renderer2D.load(shader2D);
        RectangleRenderer2D.load(shaderRectangle2D);

        FramebufferTiledSpriteSheet.setSpriteShader(spriteSheetShader);
    }

    @Override
    public void onUnload()
    {
        FramebufferTiledSpriteSheet.setSpriteShader(null);

        spriteSheetShader.dispose();
        shaderRectangle2D.dispose();
        shader2D.dispose();

        RectangleRenderer2D.unload();
        Renderer2D.unload();
    }
}
