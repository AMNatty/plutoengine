package cz.tefek.pluto.engine.graphics.texture;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;
import cz.tefek.pluto.tpl.TPL;
import cz.tefek.pluto.tpl.TPNImage;

public abstract class Texture
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

    public void delete()
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

    public void load(String file)
    {
        var wrap = new WrapMode[this.dimensions];
        Arrays.fill(wrap, this.getDefaultWrapMode());
        this.load(file, MagFilter.LINEAR, MinFilter.LINEAR, wrap);
    }

    public void load(ResourceAddress file)
    {
        var wrap = new WrapMode[this.dimensions];
        Arrays.fill(wrap, this.getDefaultWrapMode());
        this.load(file.toPath(), MagFilter.LINEAR, MinFilter.LINEAR, wrap);
    }

    public void load(ResourceAddress file, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.load(file.toPath(), magFilter, minFilter, wrap);
    }

    @Deprecated
    public void load(String file, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        TPNImage image = TPL.load(file);
        this.load(image, magFilter, minFilter, wrap);
    }

    public void load(BufferedImage imageIn, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        TPNImage image = TPL.loadImage(imageIn);
        this.load(image, magFilter, minFilter, wrap);
    }

    public void load(TPNImage image, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.load(image.getData(), image.getWidth(), image.getHeight(), magFilter, minFilter, wrap);
    }

    public void load(ByteBuffer imageData, int width, int height, MagFilter magFilter, MinFilter minFilter, WrapMode... wrap)
    {
        this.width = width;
        this.height = height;

        this.bind();

        GL33.glTexParameteriv(this.type, GL33.GL_TEXTURE_SWIZZLE_RGBA, new int[] { GL33.GL_ALPHA, GL33.GL_BLUE, GL33.GL_GREEN, GL33.GL_RED }); // TODO: Temp solution until I find a smart way to swizzle BufferedImage data

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
