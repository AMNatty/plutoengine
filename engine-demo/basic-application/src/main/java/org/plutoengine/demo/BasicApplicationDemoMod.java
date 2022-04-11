package org.plutoengine.demo;

import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.gui.STBTTFont;
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
    public static STBTTFont font;

    @Override
    public void onLoad(Mod mod)
    {
        font = STBTTFont.load(mod.getResource("fonts$robotoregular#ttf"));
    }

    @Override
    public void onUnload()
    {
        font.close();
    }
}
