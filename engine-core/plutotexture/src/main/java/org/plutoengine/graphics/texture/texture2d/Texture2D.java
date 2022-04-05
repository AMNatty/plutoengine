package org.plutoengine.graphics.texture.texture2d;

import org.lwjgl.opengl.GL33;

import org.plutoengine.graphics.texture.Texture;

/**
 * @author 493msi
 *
 */
public class Texture2D extends Texture
{
    public Texture2D()
    {
        super(GL33.GL_TEXTURE_2D, 2);
    }

    @Override
    public void writeData(long address)
    {
        GL33.glTexImage2D(this.type, 0, GL33.GL_RGBA8, this.width, this.height, 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, address);
    }

    @Override
    public boolean supportsMipMapping()
    {
        return true;
    }
}
