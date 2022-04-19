package org.plutoengine.graphics;

import org.joml.Matrix3f;
import org.joml.primitives.Rectanglef;
import org.plutoengine.graphics.gui.PlutoGUICommandParser;
import org.plutoengine.graphics.gui.font.PlutoFont;
import org.plutoengine.libra.command.LiCommandBuffer;
import org.plutoengine.libra.command.impl.LiCommandSetTransform;
import org.plutoengine.libra.text.LiTextInfo;
import org.plutoengine.libra.text.font.LiFontFamily;
import org.plutoengine.libra.text.shaping.TextShaper;
import org.plutoengine.libra.text.shaping.TextStyleOptions;

import java.util.EnumSet;

public class ImmediateFontRenderer
{
    public static <T extends PlutoFont<T>> void drawString(float x, float y, String text, LiFontFamily<T> fontFamily, TextStyleOptions style)
    {
        var font = style.pickFont(fontFamily);
        var shaper = font.getDefaultShaper();
        var info = shaper.shape(EnumSet.of(TextShaper.EnumFeature.KERNING), font, text, style);

        draw(x, y, info, style);
    }

    public static <T extends PlutoFont<T>> void drawStringNoKern(float x, float y, String text, LiFontFamily<T> fontFamily, TextStyleOptions style)
    {
        var font = style.pickFont(fontFamily);
        var shaper = font.getDefaultShaper();
        var info = shaper.shape(EnumSet.noneOf(TextShaper.EnumFeature.class), font, text, style);

        draw(x, y, info, style);
    }

    public static void draw(float x, float y, LiTextInfo info, TextStyleOptions style)
    {
        var transformBuf = LiCommandBuffer.uncleared();

        var fitBox = style.getFitBox();

        var initialScale = style.getSize() / PlutoFont.NORMALIZED_PIXEL_HEIGHT;
        var scaleX = initialScale;
        var scaleY = initialScale;

        var bounds = info.getBoundingBox().scale(scaleX, scaleY, new Rectanglef());

        if (fitBox != null)
        {
            // Rescale in sync in both are set to scale
            if (style.getOverflowX() == TextStyleOptions.OverflowXStrategy.SCALE_TO_FIT &&
                style.getOverflowY() == TextStyleOptions.OverflowYStrategy.SCALE_TO_FIT)
            {
                var smaller = Math.min(fitBox.lengthX() / bounds.lengthX(), fitBox.lengthY() / bounds.lengthY());
                var rescale = Math.min(1.0f, smaller);
                scaleX *= rescale;
                bounds.scale(rescale, rescale);
            }
            else
            {
                if (style.getOverflowX() == TextStyleOptions.OverflowXStrategy.SCALE_TO_FIT)
                {
                    var rescale = Math.min(1.0f, fitBox.lengthX() / bounds.lengthX());
                    scaleX *= rescale;
                    bounds.scale(rescale, 1.0f);
                }

                if (style.getOverflowY() == TextStyleOptions.OverflowYStrategy.SCALE_TO_FIT)
                {
                    var rescale = Math.min(1.0f, fitBox.lengthY() / bounds.lengthY());
                    scaleY *= rescale;
                    bounds.scale(1.0f, rescale);
                }
            }

            x += switch (style.getHorizontalAlign()) {
                case START -> fitBox.minX - bounds.minX;
                case CENTER -> (fitBox.maxX + fitBox.minX) / 2.0f - bounds.lengthX() / 2.0f;
                case END -> fitBox.maxX - bounds.lengthX();
            };

            y += switch (style.getVerticalAlign()) {
                case START -> fitBox.minY - bounds.minY;
                case CENTER -> (fitBox.maxY + fitBox.minY) / 2.0f - bounds.lengthY() / 2.0f;
                case END -> fitBox.maxY - bounds.lengthY();
            };
        }
        else
        {
            x += switch (style.getHorizontalAlign()) {
                case START -> -bounds.minX;
                case CENTER -> -bounds.minX + -bounds.lengthX() / 2.0f;
                case END -> -bounds.lengthX();
            };

            y += switch (style.getVerticalAlign()) {
                case START -> -bounds.lengthY();
                case CENTER -> -bounds.lengthY() / 2.0f;
                case END -> 0;
            };
        }

        transformBuf.push(new LiCommandSetTransform(new Matrix3f().scale(scaleX, scaleY, 1.0f).m20(x).m21(y)));

        var buf = info.getDrawCommandBuffer();
        var commandParser = new PlutoGUICommandParser();
        commandParser.add(LiCommandBuffer.cleared());
        commandParser.add(transformBuf);
        commandParser.add(buf);

        try (var drawCalls = commandParser.parse())
        {
            drawCalls.render();
        }
    }
}
