package org.plutoengine.graphics.gui.font.bitmap;

import org.joml.primitives.Rectanglef;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.gui.command.PlutoCommandDrawMeshDirectBuffer;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchShader;
import org.plutoengine.graphics.gui.command.PlutoCommandSwitchTexture;
import org.plutoengine.libra.command.LiCommandBuffer;
import org.plutoengine.libra.command.impl.LiCommandSetPaint;
import org.plutoengine.libra.text.LiTextInfo;
import org.plutoengine.libra.text.font.GlyphInfo;
import org.plutoengine.libra.text.shaping.IShapingStrategy;
import org.plutoengine.libra.text.shaping.TextShaper;
import org.plutoengine.libra.text.shaping.TextStyleOptions;

import java.util.EnumSet;
import java.util.Objects;

public class BitmapTextShaper implements IShapingStrategy<BitmapFont.PlutoGlyphMetrics, BitmapFont.PlutoGlyphAtlas, BitmapFont>
{
    private LiCommandBuffer commandBuffer;

    public BitmapTextShaper setCommandBuffer(LiCommandBuffer commandBuffer)
    {
        this.commandBuffer = commandBuffer;

        return this;
    }

    @Override
    public LiTextInfo shape(EnumSet<TextShaper.EnumFeature> features, BitmapFont font, String text, TextStyleOptions style)
    {
        var commandBuf = Objects.requireNonNullElseGet(this.commandBuffer, LiCommandBuffer::uncleared);

        var atlas = font.getGlyphAtlas();
        var atlasTexture = atlas.getGlyphAtlasTexture();
        var texSwitch = new PlutoCommandSwitchTexture(atlasTexture);
        commandBuf.push(texSwitch);

        var shader = PlutoGUIMod.bitmapFontShader;
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

        var indexBuf = MemoryUtil.memAllocInt(twoTriVerts * cpCount);

        var cpIt = text.codePoints().iterator();

        var indices = new int[] {
            0, 1, 2,
            0, 2, 3
        };

        float[] vertices = new float[vertDim * quadVerts];
        float[] uvs = new float[uvDim * quadVerts];

        float scale = font.getScale();

        float x = 0;
        float y = 0;

        int padding = font.getPadding();

        GlyphInfo<?, ?> info;
        BitmapFont.PlutoGlyphMetrics metrics = null;
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

                float xLow = gx - padding;
                float xHigh = gx + metrics.getCX1() * scale - metrics.getCX0() * scale + padding;
                float yLow = gy - padding;
                float yHigh = gy + metrics.getCY1() * scale - metrics.getCY0() * scale + padding;

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
                uvs[3] = uvs[1] = atlasTexture.getHeight() - uvRect.maxY;
                uvs[4] = uvs[2] = uvRect.maxX;
                uvs[7] = uvs[5] = atlasTexture.getHeight() - uvRect.minY;

                vertexBuf.put(vertices);
                uvBuf.put(uvs);

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
        mesh.addAttribute(shader.paintUVCoords, paintUVBuf.flip(), uvDim);

        mesh.addIndices(indexBuf.flip());

        commandBuf.push(mesh);

        return new LiTextInfo(commandBuf, new Rectanglef(minX, minY, maxX, maxY));
    }
}
