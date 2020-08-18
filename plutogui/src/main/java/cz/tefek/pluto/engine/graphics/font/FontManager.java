package cz.tefek.pluto.engine.graphics.font;

import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.nio.file.Files;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;
import cz.tefek.pluto.engine.graphics.texture.MagFilter;
import cz.tefek.pluto.engine.graphics.texture.MinFilter;
import cz.tefek.pluto.engine.graphics.texture.Texture;
import cz.tefek.pluto.engine.graphics.texture.WrapMode;
import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;
import cz.tefek.pluto.engine.gui.font.CharacterInfo;
import cz.tefek.pluto.engine.gui.font.Font;

public class FontManager
{
    private static Map<String, Font> fonts = new HashMap<>();

    public static void loadFont(ResourceAddress address)
    {
        String fontname = null;
        int width = 0;
        int height = 0;
        var def = new HashMap<Character, CharacterInfo>();

        int row = 0;

        try (BufferedReader br = Files.newBufferedReader(address.copy().branch("definitions").fileExtension("txt").toNIOPath()))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("//"))
                {
                    continue;
                }

                if (row == 0)
                {
                    String[] fontinfo;
                    fontinfo = line.split(",");

                    fontname = fontinfo[0];

                    String[] dim = fontinfo[1].split("x");

                    width = Integer.parseInt(dim[0]);
                    height = Integer.parseInt(dim[1]);
                }

                if (row > 0)
                {
                    String[] offs = null;

                    offs = line.split(" ")[1].split(";");

                    def.put(line.charAt(0), new CharacterInfo(row - 1, Integer.parseInt(offs[0]), Integer.parseInt(offs[1])));
                }

                row++;
            }

            br.close();
        }
        catch (Exception e)
        {
            Logger.log(SmartSeverity.ERROR, "Could not load font: " + address.toString());
            e.printStackTrace();
        }

        Font font = new Font(fontname, width, height, def);
        RectangleTexture texture = new RectangleTexture();
        texture.load(address.copy().branch("tex").fileExtension("png"), MagFilter.NEAREST, MinFilter.LINEAR, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
        font.setTexture(texture);

        fonts.put(fontname, font);
    }

    public static void unloadAll()
    {
        fonts.values().stream().map(Font::getTexture).forEach(Texture::delete);
        fonts.clear();
    }

    public static Font getFontByName(String fontname)
    {
        var font = fonts.get(fontname);

        if (font == null)
        {
            Logger.log(SmartSeverity.WARNING, "Font with name " + fontname + " could not be found, using the default one instead (if there is one).");
            return fonts.get("default");
        }

        return font;
    }
}
