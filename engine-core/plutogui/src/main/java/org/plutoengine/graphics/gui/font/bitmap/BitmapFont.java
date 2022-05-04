/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine.graphics.gui.font.bitmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.joml.primitives.Rectanglef;
import org.plutoengine.graphics.gui.font.PlutoFont;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;
import org.plutoengine.libra.text.font.GlyphInfo;
import org.plutoengine.libra.text.shaping.IShapingStrategy;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class BitmapFont extends PlutoFont<BitmapFont>
{
    private int padding;

    private BitmapFont(String name)
    {
        super(name);
    }

    public int getPadding()
    {
        return this.padding;
    }

    @Override
    public IShapingStrategy<BitmapFont.PlutoGlyphMetrics, BitmapFont.PlutoGlyphAtlas, BitmapFont> getDefaultShaper()
    {
        return new BitmapTextShaper();
    }

    public static BitmapFont load(Path path)
    {
        try
        {
            var objectMapper = new ObjectMapper(YAMLFactory.builder().build());
            BitmapFontInfo info;
            try (var br = Files.newBufferedReader(path))
            {
                info = objectMapper.readValue(br, BitmapFontInfo.class);
            }

            var font = new BitmapFont(info.name());
            Logger.logf(SmartSeverity.ADDED, "Loading font: %s%n", font.getName());

            font.atlas = font.new PlutoGlyphAtlas();

            var tex = new RectangleTexture();
            var dir = path.getParent();
            var texPath = dir.resolve(info.atlas());

            var magFilter = switch (info.filtering()) {
                case NEAREST -> MagFilter.NEAREST;
                case LINEAR -> MagFilter.LINEAR;
            };

            tex.load(texPath, magFilter, MinFilter.LINEAR, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);

            font.atlas.setGlyphAtlasTexture(tex);

            var meta = info.meta();

            font.ascent = meta.ascent();
            font.descent = meta.descent();
            font.lineGap = meta.lineGap();
            font.lineAdvance = font.ascent - font.descent + font.lineGap;
            font.scale = 1.0f / meta.scale() * PlutoFont.NORMALIZED_PIXEL_HEIGHT;

            var kern = info.kern();

            font.kerningTable = new HashMap<>();
            kern.forEach(e -> font.kerningTable.put(new PlutoFont.KerningPair(e.left(), e.right()), e.offset()));

            var glyphs = info.glyphs();
            font.glyphIndexLookup = new HashMap<>();

            font.padding = meta.padding();

            glyphs.forEach(glyph -> {
                var sprite = glyph.sprite();

                var cp = glyph.cp();

                var glyphMetrics = font.new PlutoGlyphMetrics(cp) {{
                    this.advanceX = glyph.advance();
                    this.leftSideBearing = glyph.leftBearing();

                    this.xOrigin = (this.leftSideBearing - font.padding) * PlutoFont.NORMALIZED_PIXEL_HEIGHT / meta.scale();
                    this.yOrigin = (glyph.topOffset() - font.ascent - font.padding) * PlutoFont.NORMALIZED_PIXEL_HEIGHT / meta.scale();

                    this.cx0 = this.leftSideBearing + font.padding;
                    this.cy0 = glyph.topOffset() - font.ascent + font.padding;
                    this.cx1 = this.leftSideBearing + sprite.w() - font.padding;
                    this.cy1 = sprite.h() - font.ascent - font.padding;
                }};

                var rect = new Rectanglef(
                    sprite.x(),
                    sprite.y(),
                    sprite.x() + sprite.w(),
                    sprite.y() + sprite.h()
                );

                var glyphInfo = new GlyphInfo<>(font.atlas, 0, rect);

                font.addGlyph(cp, glyphMetrics, glyphInfo);
            });

            return font;
        }
        catch (Exception e)
        {
            Logger.logf(SmartSeverity.ERROR, "Failed to load font: %s%n", path);
            Logger.log(e);

            return null;
        }
    }
}
