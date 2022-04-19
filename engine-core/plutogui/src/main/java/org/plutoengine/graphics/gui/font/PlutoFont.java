package org.plutoengine.graphics.gui.font;

import org.plutoengine.graphics.texture.Texture;
import org.plutoengine.libra.text.font.GlyphMetrics;
import org.plutoengine.libra.text.font.LiFont;
import org.plutoengine.libra.text.shaping.IShapingStrategy;

import java.util.Map;

public abstract class PlutoFont<T extends PlutoFont<? super T>> extends LiFont<PlutoFont<T>.PlutoGlyphAtlas, PlutoFont<T>.PlutoGlyphMetrics> implements AutoCloseable
{
    public static final int NORMALIZED_PIXEL_HEIGHT = 64;

    protected int descent;
    protected int ascent;
    protected int lineGap;
    protected int lineAdvance;

    protected float scale;

    protected Map<Integer, Integer> glyphIndexLookup;

    protected Map<PlutoFont.KerningPair, Integer> kerningTable;

    public record KerningPair(int left, int right)
    {

    }

    protected PlutoFont(String name)
    {
        super(name);
    }

    public abstract IShapingStrategy<? extends PlutoFont<? super T>.PlutoGlyphMetrics, ? extends PlutoFont<? super T>.PlutoGlyphAtlas, T> getDefaultShaper();

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
        var sk = new PlutoFont.KerningPair(glyphIndexLookup.getOrDefault(left, -1), glyphIndexLookup.getOrDefault(right, -1));
        return this.kerningTable.getOrDefault(sk, 0);
    }

    @Override
    public void close()
    {
        var tex = this.getGlyphAtlas().getGlyphAtlasTexture();
        tex.close();
    }

    public class PlutoGlyphAtlas extends LiFont<PlutoFont<T>.PlutoGlyphAtlas, PlutoFont<T>.PlutoGlyphMetrics>.GlyphAtlas
    {
        private Texture glyphAtlasTexture;

        public void setGlyphAtlasTexture(Texture glyphAtlasTexture)
        {
            this.glyphAtlasTexture = glyphAtlasTexture;
        }

        public Texture getGlyphAtlasTexture()
        {
            return this.glyphAtlasTexture;
        }
    }

    public class PlutoGlyphMetrics extends GlyphMetrics
    {
        private final int codepoint;
        protected int advanceX;
        protected int leftSideBearing;

        protected int cx0;
        protected int cy0;
        protected int cx1;
        protected int cy1;

        protected int xOrigin;
        protected int yOrigin;

        public PlutoGlyphMetrics(int codepoint)
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
            return PlutoFont.this.getKerningOffset(this.codepoint, cp);
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
}
