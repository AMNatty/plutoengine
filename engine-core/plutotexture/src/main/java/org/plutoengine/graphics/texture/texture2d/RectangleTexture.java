package org.plutoengine.graphics.texture.texture2d;

import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.texture.Texture;
import org.plutoengine.graphics.texture.WrapMode;

import java.util.Arrays;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public class RectangleTexture extends Texture
{
    public RectangleTexture()
    {
        super(GL33.GL_TEXTURE_RECTANGLE, 2);
    }

    @Override
    public boolean supportsMipMapping()
    {
        return false;
    }

    @Override
    public Texture setWrapOptions(WrapMode... wrapOptions)
    {
        if (Arrays.stream(wrapOptions).anyMatch(WrapMode.repeatModes::contains))
        {
            Logger.log(SmartSeverity.ERROR, "Error: Rectangle textures do not support repeat wrap modes!");

            return this;
        }

        return super.setWrapOptions(wrapOptions);
    }

    @Override
    public void writeData(long address)
    {
        GL33.glTexImage2D(this.type, 0, GL33.GL_RGBA8, this.width, this.height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, address);
    }

    @Override
    protected WrapMode getDefaultWrapMode()
    {
        return WrapMode.CLAMP_TO_EDGE;
    }
}
