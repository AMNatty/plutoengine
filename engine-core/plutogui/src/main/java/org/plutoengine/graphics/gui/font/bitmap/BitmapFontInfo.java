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
