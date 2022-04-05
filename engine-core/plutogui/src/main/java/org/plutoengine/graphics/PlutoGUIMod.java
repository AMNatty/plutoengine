package org.plutoengine.graphics;

import org.plutoengine.Pluto;
import org.plutoengine.graphics.font.FontManager;
import org.plutoengine.graphics.font.FontShader;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;
import org.plutoengine.gui.font.FontRenderer;
import org.plutoengine.mod.IModEntryPoint;
import org.plutoengine.mod.Mod;
import org.plutoengine.mod.ModEntry;
import org.plutoengine.shader.RenderShaderBuilder;

/**
 * @author 493msi
 *
 */
@ModEntry(modID = PlutoGUIMod.MOD_ID,
        dependencies = { PlutoSpriteSheetMod.class },
        version = Pluto.VERSION)
public class PlutoGUIMod implements IModEntryPoint
{
    public static final String MOD_ID = "tefek.plutogui";

    public static Mod instance;

    public static RectangleTexture uiElementsAtlas;

    private static FontShader fontShader;

    public void onLoad(Mod mod)
    {
        instance = mod;

        fontShader = new RenderShaderBuilder(mod.getResource("shaders.VertexFontShader#glsl"), mod.getResource("shaders.FragmentFontShader#glsl")).build(FontShader.class, false);

        // Load the default font
        FontManager.loadFont(mod.getResource("font.default"));

        FontRenderer.load(fontShader);

        uiElementsAtlas = new RectangleTexture();
        uiElementsAtlas.load(mod.getResource("gui.elements#png"), MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
    }

    public void onUnload()
    {
        uiElementsAtlas.delete();

        FontManager.unloadAll();

        FontRenderer.unload();
        fontShader.dispose();
    }
}
