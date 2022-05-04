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

package org.plutoengine.graphics.gui.font.stbttf;

import org.joml.primitives.Rectanglef;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;
import org.plutoengine.buffer.BufferHelper;
import org.plutoengine.graphics.gui.SDFTextureArray;
import org.plutoengine.graphics.gui.font.PlutoFont;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.libra.text.font.GlyphInfo;
import org.plutoengine.libra.text.shaping.IShapingStrategy;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class STBTTFont extends PlutoFont<STBTTFont>
{
    public static final int SDF_PADDING = 8;
    public static final int SHEET_SIZE = 1024;

    private STBTTFont(String name)
    {
        super(name);
    }

    @Override
    public IShapingStrategy<STBTTFont.PlutoGlyphMetrics, STBTTFont.PlutoGlyphAtlas, STBTTFont> getDefaultShaper()
    {
        return new STBTTTextShaper();
    }

    public static STBTTFont load(Path path)
    {
        try (var stack = MemoryStack.stackPush())
        {
            var fontInfo = STBTTFontinfo.calloc(stack);

            var data = BufferHelper.readToByteBuffer(path);

            if (!STBTruetype.stbtt_InitFont(fontInfo, data))
            {
                Logger.logf(SmartSeverity.ERROR, "Failed to load font: %s%n", path);

                return null;
            }

            var nameBuf = STBTruetype.stbtt_GetFontNameString(fontInfo,
                STBTruetype.STBTT_PLATFORM_ID_MICROSOFT,
                STBTruetype.STBTT_MS_EID_UNICODE_BMP,
                STBTruetype.STBTT_MS_LANG_ENGLISH,
                1);

            if (nameBuf == null)
            {
                Logger.logf(SmartSeverity.ERROR, "Failed to load font: %s%n", path);

                return null;
            }

            var name = StandardCharsets.UTF_16BE.decode(nameBuf).toString();

            Logger.logf(SmartSeverity.ADDED, "Loading font: %s%n", name);

            var font = new STBTTFont(name);
            font.scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, NORMALIZED_PIXEL_HEIGHT);

            var ascentBuf = stack.callocInt(1);
            var descentBuf = stack.callocInt(1);
            var lineGapBuf = stack.callocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascentBuf, descentBuf, lineGapBuf);

            var codepoints = IntStream.rangeClosed(0x0000, 0x04ff);

            var sdfWidthBuf = stack.mallocInt(1);
            var sdfHeightBuf = stack.mallocInt(1);
            var xOffsBuf = stack.mallocInt(1);
            var yOffsBuf = stack.mallocInt(1);

            var rectPacker = STBRPContext.malloc(stack);
            var nodes = STBRPNode.calloc(SHEET_SIZE * 4);

            STBRectPack.stbrp_init_target(rectPacker, SHEET_SIZE, SHEET_SIZE, nodes);
            STBRectPack.stbrp_setup_allow_out_of_mem(rectPacker, true);
            var rect = STBRPRect.malloc(1, stack);

            var sheets = new ArrayList<BufferedImage>();

            var atlas = new BufferedImage(SHEET_SIZE, SHEET_SIZE, BufferedImage.TYPE_BYTE_GRAY);
            var graphics = atlas.getGraphics();
            var colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            var colorModel = new ComponentColorModel(colorSpace, new int[] { 8 }, false,false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

            font.atlas = font.new PlutoGlyphAtlas();
            font.glyphIndexLookup = new HashMap<>();

            var advanceWidth = stack.mallocInt(1);
            var leftSideBearingBuf = stack.mallocInt(1);

            var cx0Buf = stack.mallocInt(1);
            var cy0Buf = stack.mallocInt(1);
            var cx1Buf = stack.mallocInt(1);
            var cy1Buf = stack.mallocInt(1);

            var onedgeValue = 128;
            var pixelDistScale = onedgeValue / (float) SDF_PADDING;

            for (var cp : codepoints.toArray())
            {
                var buf = STBTruetype.stbtt_GetCodepointSDF(fontInfo,
                    font.scale,
                    cp,
                    SDF_PADDING,
                    (byte) onedgeValue,
                    pixelDistScale,
                    sdfWidthBuf,
                    sdfHeightBuf,
                    xOffsBuf,
                    yOffsBuf);

                var width = sdfWidthBuf.get(0);
                var height = sdfHeightBuf.get(0);

                var glyphInfo = (GlyphInfo<STBTTFont.PlutoGlyphAtlas, STBTTFont.PlutoGlyphMetrics>) null;

                if (buf != null)
                {

                    rect.w(width);
                    rect.h(height);

                    if (STBRectPack.stbrp_pack_rects(rectPacker, rect) == 0)
                    {
                        sheets.add(atlas);

                        atlas = new BufferedImage(SHEET_SIZE, SHEET_SIZE, BufferedImage.TYPE_BYTE_GRAY);
                        graphics = atlas.getGraphics();

                        STBRectPack.stbrp_init_target(rectPacker, SHEET_SIZE, SHEET_SIZE, nodes);
                        STBRectPack.stbrp_setup_allow_out_of_mem(rectPacker, true);

                        STBRectPack.stbrp_pack_rects(rectPacker, rect);
                    }

                    var dataBuf = new DataBuffer(DataBuffer.TYPE_BYTE, width * height) {
                        @Override
                        public int getElem(int bank, int i)
                        {
                            return buf.get(i);
                        }

                        @Override
                        public void setElem(int bank, int i, int val)
                        {
                            buf.put(i, (byte) val);
                        }
                    };

                    var sampleModel = colorModel.createCompatibleSampleModel(width, height);
                    var raster = new WritableRaster(sampleModel, dataBuf, new Point()) {};
                    var image = new BufferedImage(colorModel, raster, false, null);
                    graphics.drawImage(image, rect.x(), rect.y(), null);

                    var glyphRect = new Rectanglef(
                        rect.x() / (float) SHEET_SIZE,
                        rect.y() / (float) SHEET_SIZE,
                        (rect.x() + rect.w()) / (float) SHEET_SIZE,
                        (rect.y() + rect.h()) / (float) SHEET_SIZE);

                    glyphInfo = new GlyphInfo<>(font.atlas, sheets.size(), glyphRect);

                    STBTruetype.stbtt_FreeSDF(buf);
                }

                STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, cp, advanceWidth, leftSideBearingBuf);

                var glyphMetrics = font.new PlutoGlyphMetrics(cp) {{
                    this.advanceX = advanceWidth.get(0);
                    this.leftSideBearing = leftSideBearingBuf.get(0);

                    this.xOrigin = xOffsBuf.get(0);
                    this.yOrigin = yOffsBuf.get(0);

                    STBTruetype.stbtt_GetCodepointBox(fontInfo, cp, cx0Buf, cy0Buf, cx1Buf, cy1Buf);
                    this.cx0 = cx0Buf.get(0);
                    this.cy0 = cy0Buf.get(0);
                    this.cx1 = cx1Buf.get(0);
                    this.cy1 = cy1Buf.get(0);
                }};

                font.addGlyph(cp, glyphMetrics, glyphInfo);

                font.glyphIndexLookup.put(cp, STBTruetype.stbtt_FindGlyphIndex(fontInfo, cp));
            }

            if (!sheets.contains(atlas))
                sheets.add(atlas);

            nodes.free();

            var kerningTableLength = STBTruetype.stbtt_GetKerningTableLength(fontInfo);
            try (var kerningTable = STBTTKerningentry.malloc(kerningTableLength))
            {
                STBTruetype.stbtt_GetKerningTable(fontInfo, kerningTable);
                font.kerningTable = new HashMap<>();
                kerningTable.forEach(e -> font.kerningTable.put(new PlutoFont.KerningPair(e.glyph1(), e.glyph2()), e.advance()));
            }

            font.ascent = ascentBuf.get(0);
            font.descent = descentBuf.get(0);
            font.lineGap = lineGapBuf.get(0);
            font.lineAdvance = font.ascent - font.descent + font.lineGap;
            var tex = new SDFTextureArray();
            tex.loadImg(sheets, SHEET_SIZE, SHEET_SIZE, sheets.size(), MagFilter.LINEAR, MinFilter.LINEAR, WrapMode.MIRROR_CLAMP_TO_EDGE, WrapMode.MIRROR_CLAMP_TO_EDGE);
            font.atlas.setGlyphAtlasTexture(tex);

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
