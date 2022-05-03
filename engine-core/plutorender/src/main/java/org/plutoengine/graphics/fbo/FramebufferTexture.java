package org.plutoengine.graphics.fbo;

import org.lwjgl.system.MemoryUtil;

import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

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
