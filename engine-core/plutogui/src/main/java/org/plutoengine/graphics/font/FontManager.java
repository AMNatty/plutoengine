package org.plutoengine.graphics.font;

import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.Texture;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;
import org.plutoengine.gui.font.CharacterInfo;
import org.plutoengine.gui.font.Font;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FontManager
{
    private static final Map<String, Font> fonts = new HashMap<>();

    public static void loadFont(Path address)
    {
        String fontname = null;
        int width = 0;
        int height = 0;
        var def = new HashMap<Character, CharacterInfo>();

        int row = 0;

        try
        {
            var lines = Files.readAllLines(address.resolve("definitions#txt"));

            for (var line : lines)
            {
                if (line.startsWith("//"))
                    continue;

                if (row == 0)
                {
                    String[] fontinfo = line.split(",");

                    fontname = fontinfo[0];

                    String[] dim = fontinfo[1].split("x");

                    width = Integer.parseInt(dim[0]);
                    height = Integer.parseInt(dim[1]);
                }
                else
                {
                    String[] offs = line.split(" ")[1].split(";");

                    def.put(line.charAt(0), new CharacterInfo(row - 1, Integer.parseInt(offs[0]), Integer.parseInt(offs[1])));
                }

                row++;
            }
        }
        catch (Exception e)
        {
            Logger.log(SmartSeverity.ERROR, "Could not load font: " + address.toString());
            e.printStackTrace();
        }

        Font font = new Font(fontname, width, height, def);
        RectangleTexture texture = new RectangleTexture();
        texture.load(address.resolve("tex#png"), MagFilter.NEAREST, MinFilter.LINEAR, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
        font.setTexture(texture);

        fonts.put(fontname, font);
    }

    public static void unloadAll()
    {
        fonts.values()
             .stream()
             .map(Font::getTexture)
             .forEach(Texture::delete);
        fonts.clear();
    }

    public static Font getFontByName(String fontname)
    {
        var font = fonts.get(fontname);

        if (font == null)
        {
            // Logger.log(SmartSeverity.WARNING, "Font with name " + fontname + " could not be found, using the default one instead (if there is one).");
            return fonts.get("default");
        }

        return font;
    }
}
