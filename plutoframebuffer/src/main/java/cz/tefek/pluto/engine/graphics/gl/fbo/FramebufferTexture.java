package cz.tefek.pluto.engine.graphics.gl.fbo;

import org.lwjgl.system.MemoryUtil;

import cz.tefek.pluto.engine.graphics.texture.MagFilter;
import cz.tefek.pluto.engine.graphics.texture.MinFilter;
import cz.tefek.pluto.engine.graphics.texture.WrapMode;
import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;

public class FramebufferTexture extends RectangleTexture
{
    public FramebufferTexture(int width, int height, MagFilter magFilter, MinFilter minFilter, WrapMode wrapU, WrapMode wrapV)
    {
        this.bind();
        this.setFilteringOptions(magFilter, minFilter);
        this.setWrapOptions(wrapU, wrapV);

        this.resize(width, height);
    }

    public FramebufferTexture(int width, int height)
    {
        this(width, height, MagFilter.LINEAR, MinFilter.LINEAR, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
    }

    public void resize(int width, int height)
    {
        this.bind();

        this.width = width;
        this.height = height;
        this.writeData(MemoryUtil.NULL);
    }
}
