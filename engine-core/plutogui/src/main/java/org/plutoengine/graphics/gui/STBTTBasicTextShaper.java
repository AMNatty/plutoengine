package org.plutoengine.graphics.gui;

import org.lwjgl.system.MemoryUtil;
import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.gui.command.PlutoCommandDrawMesh;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchShader;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchTexture;
import org.plutoengine.libra.command.LiCommandBuffer;
import org.plutoengine.libra.text.LiTextInfo;
import org.plutoengine.libra.text.font.GlyphInfo;
import org.plutoengine.libra.text.shaping.IShapingStrategy;
import org.plutoengine.libra.text.shaping.TextShaper;

import java.util.Arrays;
import java.util.EnumSet;

public class STBTTBasicTextShaper implements IShapingStrategy<STBTTFont.STBTTGlyphMetrics, STBTTFont.STBTTGlyphAtlas, STBTTFont>
{

    @Override
    public LiTextInfo shape(EnumSet<TextShaper.EnumFeature> features, STBTTFont font, String text)
    {
        var commandBuf = new LiCommandBuffer();

        var atlas = font.getGlyphAtlas();
        var atlasTexture = atlas.getGlyphAtlasTexture();
        var texSwitch = new PlutoCommandSwitchTexture(atlasTexture);
        commandBuf.push(texSwitch);

        var shader = PlutoGUIMod.fontShader;
        var shaderSwitch = new PlutoCommandSwitchShader(shader);
        commandBuf.push(shaderSwitch);

        var cpCount = (int) text.codePoints().count();

        var mesh = new PlutoCommandDrawMesh();

        final var quadVerts = 4;
        final var twoTriVerts = 6;

        var vertDim = 2;
        var vertexBuf = MemoryUtil.memAllocFloat(vertDim * quadVerts * cpCount);

        var uvDim = 2;
        var uvBuf = MemoryUtil.memAllocFloat(uvDim * quadVerts * cpCount);

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

        float scale = 1 / (float) STBTTFont.PIXEL_HEIGHT;

        float x = 0;
        float y = 0;

        GlyphInfo<STBTTFont.STBTTGlyphAtlas, STBTTFont.STBTTGlyphMetrics> info;
        STBTTFont.STBTTGlyphMetrics metrics = null;
        int cp;

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

            if (metrics != null)
                x += metrics.getKerning(cp);

            metrics = font.getGlyphMetrics(cp);
            info = atlas.getGlyph(cp);

            if (metrics == null)
                continue;

            x += metrics.getLeftSideBearing() * scale;

            if (info != null)
            {
                float gx = x;
                float gy = y + font.getAscent() * scale - metrics.getCY0() * scale;

                vertices[6] = vertices[0] = gx - STBTTFont.SDF_PADDING;
                vertices[3] = vertices[1] = gy + STBTTFont.SDF_PADDING;
                vertices[4] = vertices[2] = gx + metrics.getCX1() * scale - metrics.getCX0() * scale + STBTTFont.SDF_PADDING;
                vertices[7] = vertices[5] = gy - metrics.getCY1() * scale + metrics.getCY0() * scale - STBTTFont.SDF_PADDING;

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

        mesh.addAttribute(shader.position, vertexBuf.flip(), vertDim);
        mesh.addAttribute(shader.uvCoords, uvBuf.flip(), uvDim);
        mesh.addAttribute(shader.page, pageBuf.flip(), pageDim);

        mesh.addIndices(indexBuf.flip());

        commandBuf.push(mesh);

        return new PlutoTextInfo(commandBuf);
    }
}
