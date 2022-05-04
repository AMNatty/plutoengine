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

package org.plutoengine.graphics.texture;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;
import org.plutoengine.tpl.ImageABGR;
import org.plutoengine.tpl.ImageLoader;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Arrays;

public abstract class Texture implements AutoCloseable
{
    protected int glID;
    protected final int type;
    protected final int dimensions;

    protected int width = 1;
    protected int height = 1;
    protected int depth = 1;

    protected MinFilter minFilter;
    protected MagFilter magFilter;

    protected WrapMode wrapModeU;
    protected WrapMode wrapModeV;
    protected WrapMode wrapModeW;

    public Texture(int type, int dimensions)
    {
        this.glID = GL33.glGenTextures();
        this.type = type;
        this.dimensions = dimensions;

        Logger.logf(SmartSeverity.ADDED, "Texture with ID %d of type '%s' created...\n", this.glID, this.getClass().getSimpleName());
    }

    public void bind()
    {
        GL33.glBindTexture(this.type, this.glID);
    }

    public void unbind()
    {
        GL33.glBindTexture(this.type, 0);
    }

    public void close()
    {
        Logger.logf(SmartSeverity.REMOVED, "Texture with ID %d of type '%s' deleted...\n", this.glID, this.getClass().getSimpleName());

        // Delete unbinds automatically
        GL33.glDeleteTextures(this.glID);

        this.glID = 0;

        this.width = 0;
        this.height = 0;
        this.depth = 0;

        this.wrapModeU = null;
        this.wrapModeV = null;
        this.wrapModeW = null;

        this.minFilter = null;
        this.magFilter = null;
    }

    public int getDepth()
    {
        return this.depth;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public MagFilter getMagFilter()
    {
        return this.magFilter;
    }

    public MinFilter getMinFilter()
    {
        return this.minFilter;
    }

    public WrapMode getWrapModeU()
    {
        return this.wrapModeU;
    }

    public WrapMode getWrapModeV()
    {
        return this.wrapModeV;
    }

    public WrapMode getWrapModeW()
    {
        return this.wrapModeW;
    }

    public Texture setFilteringOptions(MagFilter magFilter, MinFilter minFilter)
    {
        if (minFilter.isMipMapped())
        {
            if (!this.supportsMipMapping())
            {
                Logger.logf(SmartSeverity.ERROR, "The texture of type '%s' does not support mipmaps.\n", this.getClass().getSimpleName());
                return this;
            }

            GL33.glGenerateMipmap(this.type);
        }

        this.magFilter = magFilter;
        GL33.glTexParameteri(this.type, GL33.GL_TEXTURE_MAG_FILTER, this.magFilter.getGLID());

        this.minFilter = minFilter;
        GL33.glTexParameteri(this.type, GL33.GL_TEXTURE_MIN_FILTER, this.minFilter.getGLID());

        return this;
    }

    public Texture setWrapOptions(WrapMode... wrapOptions)
    {
        if (wrapOptions.length != this.dimensions)
        {
            Logger.log(SmartSeverity.ERROR, "Error: WrapMode option count does not match texture's dimensions.");
            return this;
        }

        this.wrapModeU = wrapOptions[0];
        GL33.glTexParameteri(this.type, GL33.GL_TEXTURE_WRAP_S, this.wrapModeU.getGLID());

        if (this.dimensions >= 2)
        {
            this.wrapModeV = wrapOptions[1];
            GL33.glTexParameteri(this.type, GL33.GL_TEXTURE_WRAP_T, this.wrapModeV.getGLID());

            if (this.dimensions >= 3)
            {
                this.wrapModeW = wrapOptions[2];
                GL33.glTexParameteri(this.type, GL33.GL_TEXTURE_WRAP_R, this.wrapModeW.getGLID());
            }
        }

        return this;
    }

    public void writeData(ByteBuffer buffer)
    {
        this.writeData(MemoryUtil.memAddress(buffer));
    }

    public abstract boolean supportsMipMapping();

    public abstract void writeData(long address);

    public int getID()
    {
        return this.glID;
    }

    protected WrapMode getDefaultWrapMode()
    {
        return WrapMode.REPEAT;
    }

    public void load(Path path)
    {
        var wrap = new WrapMode[this.dimensions];
        Arrays.fill(wrap, this.getDefaultWrapMode());
        this.load(path, MagFilter.LINEAR, MinFilter.LINEAR, wrap);
    }

    public void load(Path path, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.load(ImageLoader.load(path), magFilter, minFilter, wrap);
    }

    public void load(BufferedImage imageIn, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.load(ImageLoader.loadImage(imageIn), magFilter, minFilter, wrap);
    }

    public void load(ImageABGR image, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.load(image.getData(), image.getWidth(), image.getHeight(), magFilter, minFilter, wrap);
    }

    public void load(ByteBuffer imageData, int width, int height, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.width = width;
        this.height = height;

        this.bind();

        // TODO: Temp solution until I find a smart way to swizzle BufferedImage data
        GL33.glTexParameteriv(this.type, GL33.GL_TEXTURE_SWIZZLE_RGBA, new int[] { GL33.GL_ALPHA, GL33.GL_BLUE, GL33.GL_GREEN, GL33.GL_RED });

        this.setFilteringOptions(magFilter, minFilter);
        this.setWrapOptions(wrap);
        this.writeData(imageData);
    }

    public boolean isValid()
    {
        return this.glID > 0;
    }

    public int getType()
    {
        return this.type;
    }
}
