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

package org.plutoengine.graphics.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.Texture;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.tpl.ImageLoader;
import org.plutoengine.tpl.ImageY;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class SDFTextureArray extends Texture
{
    public SDFTextureArray()
    {
        super(GL33.GL_TEXTURE_2D_ARRAY, 2);
    }

    @Override
    public boolean supportsMipMapping()
    {
        return false;
    }

    @Override
    public void writeData(long address)
    {
        GL33.glTexImage3D(GL33.GL_TEXTURE_2D_ARRAY, 0, GL33.GL_R8, this.width, this.height, this.depth, 0, GL33.GL_RED, GL11.GL_UNSIGNED_BYTE, address);
    }

    public void loadImg(List<BufferedImage> imageData, int width, int height, int depth, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        var data = imageData.stream()
                            .map(ImageLoader::loadImageGrayscale)
                            .map(ImageY::getData)
                            .toList();

        this.load(data, width, height, depth, magFilter, minFilter, wrap);
    }

    public void load(List<ByteBuffer> imageData, int width, int height, int depth, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.width = width;
        this.height = height;
        this.depth = imageData.size();

        this.bind();

        this.setFilteringOptions(magFilter, minFilter);
        this.setWrapOptions(wrap);

        this.writeData(0);

        for (int i = 0; i < imageData.size(); i++)
        {
            var img = imageData.get(i);
            GL33.glTexSubImage3D(GL33.GL_TEXTURE_2D_ARRAY, 0,
                0, 0, i,
                this.width, this.height, 1,
                GL33.GL_RED, GL11.GL_UNSIGNED_BYTE, img);
        }
    }

    @Override
    public void load(ByteBuffer imageData, int width, int height, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        throw new UnsupportedOperationException();
    }
}
