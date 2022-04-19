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
