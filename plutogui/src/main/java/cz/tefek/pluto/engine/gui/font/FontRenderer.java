package cz.tefek.pluto.engine.gui.font;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import cz.tefek.pluto.engine.graphics.font.FontManager;
import cz.tefek.pluto.engine.graphics.font.FontShader;
import cz.tefek.pluto.engine.graphics.gl.DrawMode;
import cz.tefek.pluto.engine.graphics.gl.vao.QuadPresets;
import cz.tefek.pluto.engine.graphics.gl.vao.VertexArray;
import cz.tefek.pluto.engine.math.TransformationMatrix;

public class FontRenderer
{
    private static final int LINE_HEIGHT = 30;
    private static final int SPACE_WIDTH = 12;

    private static final int CHAR_WIDTH = 16;
    private static final int CHAR_HEIGHT = 24;

    private static VertexArray charVAO;
    private static FontShader fontShader;

    public static void load(FontShader defaultFontShaderIn)
    {
        charVAO = QuadPresets.basicNoNeg();
        fontShader = defaultFontShaderIn;
    }

    public static void unload()
    {
        if (charVAO != null)
        {
            charVAO.delete();
        }
    }

    public static void prepareInstance(Font font)
    {
        GL11.glEnable(GL11.GL_BLEND);
        fontShader.start();
        charVAO.bind();
        charVAO.enableAllAttributes();
        font.getTexture().bind();
    }

    public static void drawString(float xPos, float yPos, Object string, Object color, float relativeSize, String fontname, boolean isShadow)
    {
        Font font = FontManager.getFontByName(fontname);

        if (font == null)
        {
            System.err.println("Font doesn't exist: " + fontname);
            return;
        }

        float charWidth = CHAR_WIDTH * relativeSize;
        float charHeight = CHAR_HEIGHT * relativeSize;

        float lineHeight = LINE_HEIGHT * relativeSize;
        float spaceWidth = SPACE_WIDTH * relativeSize;

        prepareInstance(font);

        color(color);

        String text = String.valueOf(string);

        float drawX = xPos;
        float drawY = yPos;

        for (int characterIndex = 0; characterIndex < text.length(); characterIndex++)
        {
            int column = 0;
            int row = 0;

            var currentChar = text.charAt(characterIndex);

            if (text.length() > characterIndex + 1)
            {
                var nextChar = text.charAt(characterIndex + 1);

                if (currentChar == '\\' && nextChar == '&')
                {
                    continue;
                }

                // Inline coloring (tm) :) -> &c[0xff770077]
                if (text.length() > characterIndex + 13)
                {
                    if (currentChar == '&' && nextChar == 'c' && text.charAt(characterIndex + 2) == '[' && text.charAt(characterIndex + 13) == ']')
                    {
                        char c = '0';
                        char cBef = '0';

                        if (characterIndex > 0)
                        {
                            c = text.charAt(characterIndex - 1);
                        }

                        if (characterIndex > 1)
                        {
                            cBef = text.charAt(characterIndex - 2);
                        }

                        if (c != '\\' || cBef == '\\' && c == '\\')
                        {
                            if (!isShadow)
                            {
                                char[] col = new char[10];

                                text.getChars(characterIndex + 3, characterIndex + 13, col, 0);

                                String clr = String.valueOf(col);

                                color(clr);
                            }

                            characterIndex += 13;

                            continue;
                        }
                    }
                }

                if (text.length() > characterIndex + 2)
                {
                    if (currentChar == '&' && nextChar == 'i' && (text.charAt(characterIndex + 2) == '1' || text.charAt(characterIndex + 2) == '0'))
                    {
                        char c = '0';
                        char cBef = '0';

                        if (characterIndex > 0)
                        {
                            c = text.charAt(characterIndex - 1);
                        }

                        if (characterIndex > 1)
                        {
                            cBef = text.charAt(characterIndex - 2);
                        }

                        if (c != '\\' || cBef == '\\' && c == '\\')
                        {
                            if (text.charAt(characterIndex + 2) == '1')
                            {
                                italic(true);
                            }
                            else
                            {
                                italic(false);
                            }

                            characterIndex += 2;

                            continue;
                        }
                    }
                }
            }

            float shift = 0;

            switch (currentChar)
            {
                case '\n':
                    color(color);
                    drawX = xPos;
                    drawY += lineHeight;
                    continue;

                case ' ':
                    drawX += spaceWidth;
                    continue;

                case 'g':
                case 'y':
                case 'p':
                case 'j':
                    shift = 6 * relativeSize;

                    break;

                default:
                    break;
            }

            var fontDefs = font.getDefinitions();
            var charInf = fontDefs.get(currentChar);

            if (charInf == null)
            {
                charInf = fontDefs.get('?');
            }

            var atlasIndex = charInf.getNumber();

            row = atlasIndex / CHAR_WIDTH;
            column = atlasIndex % CHAR_WIDTH;

            // Position of the current character in the texture atlas in pixels
            float u = column * CHAR_WIDTH;
            float v = row * CHAR_HEIGHT;

            // Offset from the left
            drawX -= charInf.getLeftOffset() * relativeSize;

            float posY = shift + drawY;

            fontShader.uvBase.load(u, font.getTextureHeight() - v - CHAR_HEIGHT);
            fontShader.uvDelta.load(CHAR_WIDTH, CHAR_HEIGHT);

            Matrix4f transformation = TransformationMatrix.create(new Vector3f(drawX, posY, 0), new Vector3f(0, 0, 0), new Vector3f(charWidth, charHeight, 0));

            fontShader.transformationMatrix.load(transformation);

            charVAO.draw(DrawMode.TRIANGLES);

            drawX += charWidth;
            drawX -= charInf.getRightOffset() * relativeSize;
            drawX += relativeSize;
        }

        italic(false);
    }

    public static void color(Object color)
    {
        color(color, false);
    }

    public static void color(Object color, boolean darker)
    {
        float dark = 0;

        if (darker)
        {
            dark = 0.35f;
        }

        if (color instanceof float[])
        {
            float[] c = (float[]) color;

            if (c.length == 4)
            {
                recolor(c[0] - dark, c[1] - dark, c[2] - dark, c[3]);
            }
            else if (c.length == 3)
            {
                recolor(c[0] - dark, c[1] - dark, c[2] - dark, 1);
            }
        }

        if (color instanceof String)
        {
            String col = (String) color;

            if (col.length() == 7)
            {
                recolor(Integer.valueOf(col.substring(1, 3), 16) / 256f - dark, Integer.valueOf(col.substring(3, 5), 16) / 256f - dark, Integer.valueOf(col.substring(5, 7), 16) / 256f - dark, 1);
            }

            if (col.length() == 10)
            {
                recolor(Integer.valueOf(col.substring(4, 6), 16) / 256f - dark, Integer.valueOf(col.substring(6, 8), 16) / 256f - dark, Integer.valueOf(col.substring(8, 10), 16) / 256f - dark, Integer.valueOf(col.substring(2, 4), 16) / 256f);
            }
        }
    }

    private static void recolor(float r, float g, float b, float a)
    {
        fontShader.recolor.load(r, g, b, a);
    }

    private static void italic(boolean useItalic)
    {
        fontShader.italic.load(useItalic);
    }

    public static void drawString(float x, float y, Object text, float r, float g, float b, float a, float size)
    {
        drawString(x, y, text, new float[] { r, g, b, a }, size, "default", false);
    }

    public static void drawString(float x, float y, Object text)
    {
        drawString(x, y, text, new float[] { 0, 0, 0, 1 }, 1, "default", false);
    }

    public static void drawString(float x, float y, Object text, float r, float g, float b, float a, float size, boolean isShadow)
    {
        drawString(x, y, text, new float[] { r, g, b, a }, size, "default", isShadow);
    }

    public static void drawString(float x, float y, Object text, float r, float g, float b, float a, float size, String fontname, boolean isShadow)
    {
        drawString(x, y, text, new float[] { r, g, b, a }, size, fontname, isShadow);
    }

    public static void drawString(float x, float y, Object text, float r, float g, float b, float a, float size, String fontname)
    {
        drawString(x, y, text, new float[] { r, g, b, a }, size, fontname, false);
    }

    public static void drawString(float xPos, float yPos, Object string, Object color, float relativeSize, String fontname)
    {
        drawString(xPos, yPos, string, color, relativeSize, fontname, false);
    }
}
