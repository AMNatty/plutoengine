package org.plutoengine.demo;

import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.gui.font.bitmap.BitmapFont;
import org.plutoengine.graphics.gui.font.stbttf.STBTTFont;
import org.plutoengine.graphics.sprite.PartialTextureSprite;
import org.plutoengine.graphics.sprite.TemporalSprite;
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

    public static LiFontFamily<BitmapFont> srCloneFont;

    public static RectangleTexture plutoLogo;

    public static RectangleTexture srCloneBoxTex;
    public static TemporalSprite srCloneBox;

    @Override
    public void onLoad(Mod mod)
    {
        font = new LiFontFamily<>();
        font.add(TextStyleOptions.STYLE_REGULAR, STBTTFont.load(mod.getResource("plutofonts$plutostardust#ttf")));

        srCloneFont = new LiFontFamily<>();
        srCloneFont.add(TextStyleOptions.STYLE_REGULAR, BitmapFont.load(mod.getResource("plutofonts$srclone.info#yaml")));

        plutoLogo = new RectangleTexture();
        plutoLogo.load(mod.getResource("icons$icon128#png"), MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);

        srCloneBoxTex = new RectangleTexture();
        srCloneBoxTex.load(mod.getResource("box#png"));

        var frames = new PartialTextureSprite[16];
        for (int i = 0; i < frames.length; i++)
            frames[i] = new PartialTextureSprite(srCloneBoxTex, 64 * i, 0, 64, 64);

        srCloneBox = new TemporalSprite(frames, 0.04f);
    }

    @Override
    public void onUnload()
    {
        srCloneBoxTex.close();

        plutoLogo.close();

        srCloneFont.close();

        font.close();
    }
}
