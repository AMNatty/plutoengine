package org.plutoengine.graphics.gui.font.bitmap;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ext.NioPathDeserializer;

import java.nio.file.Path;
import java.util.List;

record BitmapFontInfo(
    String name,
    FontInfoMeta meta,
    @JsonDeserialize(using = NioPathDeserializer.class) Path atlas,
    Filtering filtering,
    @JsonDeserialize(contentAs = KerningInfo.class) List<KerningInfo> kern,
    @JsonDeserialize(contentAs = GlyphInfo.class) List<GlyphInfo> glyphs
)
{
    enum Filtering
    {
        NEAREST,
        LINEAR
    }

    record FontInfoMeta(
        int ascent,
        int descent,
        int lineGap,
        int padding,
        int scale
    )
    {

    }

    record KerningInfo(
        int left,
        int right,
        int offset
    )
    {

    }

    record GlyphInfo(
        int cp,
        int glyphClass,
        GlyphSprite sprite,
        int leftBearing,
        int advance,
        int topOffset
    )
    {

    }

    record GlyphSprite(
        int x,
        int y,
        int w,
        int h
    )
    {

    }
}
