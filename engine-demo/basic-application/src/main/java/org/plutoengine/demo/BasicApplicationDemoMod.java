package org.plutoengine.demo;

import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.gui.stbttf.STBTTFont;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;
import org.plutoengine.libra.text.font.LiFontFamily;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.mod.IModEntryPoint;
import org.plutoengine.mod.Mod;
import org.plutoengine.mod.ModEntry;

@ModEntry(
    modID = "tefek.demo.basicapplication",
    version = VersionInfo.GAME_VERSION,
    dependencies = { PlutoGUIMod.class }
)
public class BasicApplicationDemoMod implements IModEntryPoint
{
    public static LiFontFamily<STBTTFont> font;

    public static RectangleTexture plutoLogo;

    @Override
    public void onLoad(Mod mod)
    {
        font = new LiFontFamily<>();
        font.add(TextStyleOptions.STYLE_REGULAR, STBTTFont.load(mod.getResource("plutofonts$plutostardust#ttf")));

        plutoLogo = new RectangleTexture();
        plutoLogo.load(mod.getResource("icons$icon128#png"), MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
    }

    @Override
    public void onUnload()
    {
        plutoLogo.close();

        font.close();
    }
}
