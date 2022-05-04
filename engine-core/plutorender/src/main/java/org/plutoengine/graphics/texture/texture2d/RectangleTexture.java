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
