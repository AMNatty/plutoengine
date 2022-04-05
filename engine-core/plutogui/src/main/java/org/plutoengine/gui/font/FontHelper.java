package org.plutoengine.gui.font;

import org.plutoengine.graphics.font.FontManager;

public class FontHelper
{
    public static int calcStringWidth(Object string, String fontname, float relativeSize)
    {
        Font font = FontManager.getFontByName(fontname);

        if (font == null)
        {
            System.out.println("Font doesn't exist: " + fontname);
            return -1;
        }

        float absoluteCharWidth = 16;
        String text = string.toString();

        int maxW = 0;
        int totalSpacing = 0;

        for (int i = 0; i < text.length(); i++)
        {
            if (text.length() > i + 1)
            {
                if (text.charAt(i) == '\\' && text.charAt(i + 1) == '&')
                {
                    continue;
                }
            }

            // &c[0xff770077]
            if (text.length() > i + 13)
            {
                if (text.charAt(i) == '&' && text.charAt(i + 1) == 'c' && text.charAt(i + 2) == '[' && text.charAt(i + 13) == ']')
                {
                    char c = '0';
                    char cBef = '0';

                    if (i > 0)
                    {
                        c = text.charAt(i - 1);
                    }

                    if (i > 1)
                    {
                        cBef = text.charAt(i - 2);
                    }

                    if (c != '\\' || cBef == '\\' && c == '\\')
                    {
                        i += 13;

                        continue;
                    }
                }
            }

            if (text.length() > i + 2)
            {
                if (text.charAt(i) == '&' && text.charAt(i + 1) == 'i')
                {
                    char c = '0';
                    char cBef = '0';

                    if (i > 0)
                    {
                        c = text.charAt(i - 1);
                    }

                    if (i > 1)
                    {
                        cBef = text.charAt(i - 2);
                    }

                    if (c != '\\' || cBef == '\\' && c == '\\')
                    {
                        i += 2;

                        continue;
                    }
                }
            }

            if (text.charAt(i) == '\n')
            {
                totalSpacing = 0;
            }
            else if (text.charAt(i) == ' ')
            {
                totalSpacing += 12 * relativeSize;
            }
            else
            {
                CharacterInfo charInf = font.getDefinitions().get(text.charAt(i));

                if (charInf == null)
                {
                    charInf = font.getDefinitions().get('?');
                }

                totalSpacing -= charInf.leftOffset() * relativeSize - relativeSize;
                totalSpacing += absoluteCharWidth * relativeSize;
                totalSpacing -= charInf.rightOffset() * relativeSize - 1;
            }

            maxW = Math.max(maxW, totalSpacing);
        }

        return maxW;
    }

    public static int calcStringHeight(Object string, float relSize)
    {

        return (int) (30f * relSize * string.toString().split("\n").length);
    }

}
