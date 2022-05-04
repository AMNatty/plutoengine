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
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.gui.command.PlutoCommandDrawMeshDirectBuffer;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchShader;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchTexture;
import org.plutoengine.libra.command.LiCommandBuffer;
import org.plutoengine.libra.command.impl.LiCommandSetPaint;
import org.plutoengine.libra.command.impl.LiCommandSpecial;
import org.plutoengine.libra.text.LiTextInfo;
import org.plutoengine.libra.text.font.GlyphInfo;
import org.plutoengine.libra.text.shaping.IShapingStrategy;
import org.plutoengine.libra.text.shaping.TextShaper;
import org.plutoengine.libra.text.shaping.TextStyleOptions;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

public class STBTTTextShaper implements IShapingStrategy<STBTTFont.PlutoGlyphMetrics, STBTTFont.PlutoGlyphAtlas, STBTTFont>
{
    private LiCommandBuffer commandBuffer;

    public STBTTTextShaper setCommandBuffer(LiCommandBuffer commandBuffer)
    {
        this.commandBuffer = commandBuffer;

        return this;
    }

    @Override
    public LiTextInfo shape(EnumSet<TextShaper.EnumFeature> features, STBTTFont font, String text, TextStyleOptions style)
    {
        var commandBuf = Objects.requireNonNullElseGet(this.commandBuffer, LiCommandBuffer::uncleared);

        var atlas = font.getGlyphAtlas();
        var atlasTexture = atlas.getGlyphAtlasTexture();
        var texSwitch = new PlutoCommandSwitchTexture(atlasTexture);
        commandBuf.push(texSwitch);

        var shader = PlutoGUIMod.fontShader;
        var shaderSwitch = new PlutoCommandSwitchShader(shader);
        commandBuf.push(shaderSwitch);

        commandBuf.push(new LiCommandSetPaint(style.getPaint()));

        var cpCount = (int) text.codePoints().count();

        var mesh = new PlutoCommandDrawMeshDirectBuffer();

        final var quadVerts = 4;
        final var twoTriVerts = 6;

        var vertDim = 2;
        var vertexBuf = MemoryUtil.memAllocFloat(vertDim * quadVerts * cpCount);

        var uvDim = 2;
        var uvBuf = MemoryUtil.memAllocFloat(uvDim * quadVerts * cpCount);

        var paintUVBuf = MemoryUtil.memAllocFloat(uvDim * quadVerts * cpCount);

        var pageDim = 1;
        var pageBuf = MemoryUtil.memAllocInt(pageDim * quadVerts * cpCount);

        var indexBuf = MemoryUtil.memAllocInt(twoTriVerts * cpCount);

        var cpIt = text.codePoints().iterator();

        var indices = new int[] {
            0, 1, 2,
            0, 2, 3
        };

        float[] vertices = new float[vertDim * quadVerts];
        float[] uvs = new float[uvDim * quadVerts];
        int[] pages = new int[pageDim * quadVerts];

        float scale = font.getScale();

        commandBuf.push(new LiCommandSpecial(cb -> PlutoGUIMod.fontShader.setPixelScale(style.getSize())));

        float x = 0;
        float y = 0;

        GlyphInfo<?, ?> info;
        STBTTFont.PlutoGlyphMetrics metrics = null;
        int cp;
        float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY, minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;

        while (cpIt.hasNext())
        {
            cp = cpIt.next();

            switch (cp)
            {
                case '\n' -> {
                    x = 0;
                    y += font.getLineAdvance() * scale;
                    continue;
                }
            }

            if (metrics != null && features.contains(TextShaper.EnumFeature.KERNING))
                x += metrics.getKerning(cp) * scale;

            metrics = font.getGlyphMetrics(cp);
            info = atlas.getGlyph(cp);

            if (metrics == null)
                continue;

            if (info != null)
            {
                float gx = x + metrics.getXOrigin();
                float gy = y + metrics.getYOrigin() + font.getAscent() * scale;

                float xLow = gx - STBTTFont.SDF_PADDING;
                float xHigh = gx + metrics.getCX1() * scale - metrics.getCX0() * scale + STBTTFont.SDF_PADDING;
                float yLow = gy - STBTTFont.SDF_PADDING;
                float yHigh = gy + metrics.getCY1() * scale - metrics.getCY0() * scale + STBTTFont.SDF_PADDING;

                minX = Math.min(minX, xLow);
                minY = Math.min(minY, yLow);
                maxX = Math.max(maxX, xHigh);
                maxY = Math.max(maxY, yHigh);

                vertices[6] = vertices[0] = xLow;
                vertices[3] = vertices[1] = yHigh;
                vertices[4] = vertices[2] = xHigh;
                vertices[7] = vertices[5] = yLow;

                var uvRect = info.getRect();
                uvs[6] = uvs[0] = uvRect.minX;
                uvs[3] = uvs[1] = 1 - uvRect.maxY;
                uvs[4] = uvs[2] = uvRect.maxX;
                uvs[7] = uvs[5] = 1 - uvRect.minY;

                Arrays.fill(pages, info.getPage());

                vertexBuf.put(vertices);
                uvBuf.put(uvs);
                pageBuf.put(pages);

                indexBuf.put(indices);

                indices[0] += quadVerts;
                indices[1] += quadVerts;
                indices[2] += quadVerts;
                indices[3] += quadVerts;
                indices[4] += quadVerts;
                indices[5] += quadVerts;
            }

            x += metrics.getAdvanceX() * scale;
        }

        vertexBuf.flip();

        while (vertexBuf.hasRemaining())
        {
            paintUVBuf.put((vertexBuf.get() - minX) / (maxX - minX));
            paintUVBuf.put((vertexBuf.get() - minY) / (maxY - minY));
        }

        mesh.addAttribute(shader.position, vertexBuf.rewind(), vertDim);
        mesh.addAttribute(shader.uvCoords, uvBuf.flip(), uvDim);
        mesh.addAttribute(shader.page, pageBuf.flip(), pageDim);
        mesh.addAttribute(shader.paintUVCoords, paintUVBuf.flip(), uvDim);

        mesh.addIndices(indexBuf.flip());

        commandBuf.push(mesh);

        return new LiTextInfo(commandBuf, new Rectanglef(minX, minY, maxX, maxY));
    }
}
