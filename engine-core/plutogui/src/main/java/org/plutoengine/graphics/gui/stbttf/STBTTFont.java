package org.plutoengine.graphics.gui.stbttf;

import org.joml.primitives.Rectanglef;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;
import org.plutoengine.buffer.BufferHelper;
import org.plutoengine.graphics.gui.SDFTextureArray;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.libra.text.font.GlyphInfo;
import org.plutoengine.libra.text.font.GlyphMetrics;
import org.plutoengine.libra.text.font.LiFont;
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
import java.util.Map;
import java.util.stream.IntStream;

public class STBTTFont extends LiFont<STBTTFont.STBTTGlyphAtlas, STBTTFont.STBTTGlyphMetrics> implements AutoCloseable
{
    public static final int PIXEL_HEIGHT = 64;
    public static final int SDF_PADDING = 8;
    public static final int SHEET_SIZE = 1024;

    private int descent;
    private int ascent;
    private int lineGap;
    private int lineAdvance;

    private float scale;

    private Map<Integer, Integer> glyphIndexLookup;

    private Map<KerningPair, Integer> kerningTable;

    private record KerningPair(int left, int right)
    {

    }

    private STBTTFont(String name)
    {
        super(name);
    }

    public float getScale()
    {
        return this.scale;
    }

    public int getAscent()
    {
        return this.ascent;
    }

    public int getDescent()
    {
        return this.descent;
    }

    public int getLineGap()
    {
        return this.lineGap;
    }

    public int getLineAdvance()
    {
        return this.lineAdvance;
    }

    public int getKerningOffset(int left, int right)
    {
        var sk = new KerningPair(glyphIndexLookup.getOrDefault(left, -1), glyphIndexLookup.getOrDefault(right, -1));
        return this.kerningTable.getOrDefault(sk, 0);
    }

    @Override
    public void close()
    {
        var tex = this.getGlyphAtlas().getGlyphAtlasTexture();
        tex.close();
    }

    public class STBTTGlyphAtlas extends LiFont<STBTTGlyphAtlas, STBTTGlyphMetrics>.GlyphAtlas
    {
        private SDFTextureArray glyphAtlasTexture;

        public void setGlyphAtlasTexture(SDFTextureArray glyphAtlasTexture)
        {
            this.glyphAtlasTexture = glyphAtlasTexture;
        }

        public SDFTextureArray getGlyphAtlasTexture()
        {
            return this.glyphAtlasTexture;
        }
    }

    public class STBTTGlyphMetrics extends GlyphMetrics
    {
        private final int codepoint;
        private int advanceX;
        private int leftSideBearing;

        private int cx0;
        private int cy0;
        private int cx1;
        private int cy1;

        private int xOrigin;
        private int yOrigin;

        private STBTTGlyphMetrics(int codepoint)
        {
            this.codepoint = codepoint;
        }

        public int getAdvanceX()
        {
            return this.advanceX;
        }

        public int getLeftSideBearing()
        {
            return this.leftSideBearing;
        }

        public int getCodepoint()
        {
            return this.codepoint;
        }

        public int getKerning(int cp)
        {
            return STBTTFont.this.getKerningOffset(this.codepoint, cp);
        }

        public int getCX0()
        {
            return this.cx0;
        }

        public int getCY0()
        {
            return this.cy0;
        }

        public int getCX1()
        {
            return this.cx1;
        }

        public int getCY1()
        {
            return this.cy1;
        }

        public int getXOrigin()
        {
            return this.xOrigin;
        }

        public int getYOrigin()
        {
            return this.yOrigin;
        }
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
            font.scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, PIXEL_HEIGHT);

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

            font.atlas = font.new STBTTGlyphAtlas();
            font.glyphIndexLookup = new HashMap<>();

            var advanceWidth = stack.mallocInt(1);
            var leftSideBearing = stack.mallocInt(1);

            var cx0 = stack.mallocInt(1);
            var cy0 = stack.mallocInt(1);
            var cx1 = stack.mallocInt(1);
            var cy1 = stack.mallocInt(1);

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

                var glyphInfo = (GlyphInfo<STBTTGlyphAtlas, STBTTGlyphMetrics>) null;

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

                STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, cp, advanceWidth, leftSideBearing);

                var glyphMetrics = font.new STBTTGlyphMetrics(cp);
                glyphMetrics.advanceX = advanceWidth.get(0);
                glyphMetrics.leftSideBearing = leftSideBearing.get(0);

                glyphMetrics.xOrigin = xOffsBuf.get(0);
                glyphMetrics.yOrigin = yOffsBuf.get(0);

                STBTruetype.stbtt_GetCodepointBox(fontInfo, cp, cx0, cy0, cx1, cy1);
                glyphMetrics.cx0 = cx0.get(0);
                glyphMetrics.cy0 = cy0.get(0);
                glyphMetrics.cx1 = cx1.get(0);
                glyphMetrics.cy1 = cy1.get(0);

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
                kerningTable.forEach(e -> {
                    // System.out.printf("%s -> %s = %d%n", Character.toString(e.glyph1()), Character.toString(e.glyph2()), e.advance());

                    font.kerningTable.put(new KerningPair(e.glyph1(), e.glyph2()), e.advance());
                });
            }

            font.ascent = ascentBuf.get();
            font.descent = descentBuf.get();
            font.lineGap = lineGapBuf.get();
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
