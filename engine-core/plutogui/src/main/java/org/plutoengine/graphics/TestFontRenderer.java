package org.plutoengine.graphics;

import org.joml.Matrix3f;
import org.plutoengine.graphics.gui.PlutoGUICommandParser;
import org.plutoengine.graphics.gui.STBTTBasicTextShaper;
import org.plutoengine.graphics.gui.STBTTFont;
import org.plutoengine.libra.command.LiCommandBuffer;
import org.plutoengine.libra.command.impl.LiCommandSetTransform;
import org.plutoengine.libra.text.shaping.TextShaper;

import java.util.EnumSet;

public class TestFontRenderer
{
    public static void drawString(STBTTFont font, String text)
    {
        var shaper = new STBTTBasicTextShaper();
        var info = shaper.shape(EnumSet.of(TextShaper.EnumFeature.KERNING), font, text);

        var transformBuf = new LiCommandBuffer();
        transformBuf.push(new LiCommandSetTransform(new Matrix3f().scale(.75f).m20(400f).m21(400f)));

        var buf = info.getDrawCommandBuffer();
        var commandParser = new PlutoGUICommandParser();
        commandParser.add(transformBuf);
        commandParser.add(buf);
        var drawCalls = commandParser.parse();

        drawCalls.render();

        drawCalls.close();
    }
}
