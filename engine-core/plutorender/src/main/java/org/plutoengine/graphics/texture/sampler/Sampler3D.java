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

package org.plutoengine.graphics.texture.sampler;

import org.lwjgl.opengl.GL33;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;

import java.util.HashSet;

public class Sampler3D
{
    private final int id;

    private final HashSet<Integer> usedUnits;

    public Sampler3D()
    {
        this.id = GL33.glGenSamplers();
        this.usedUnits = new HashSet<>(GL33.glGetInteger(GL33.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));
    }

    public void setFilteringParameters(MinFilter minFilter, MagFilter magFilter, WrapMode wrapModeS, WrapMode wrapModeT, WrapMode wrapModeR)
    {
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_MIN_FILTER, minFilter.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_MAG_FILTER, magFilter.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_WRAP_S, wrapModeS.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_WRAP_T, wrapModeT.getGLID());
        GL33.glSamplerParameteri(this.id, GL33.GL_TEXTURE_WRAP_R, wrapModeR.getGLID());
    }

    public void bind(int unit)
    {
        this.usedUnits.add(unit);
        GL33.glBindSampler(unit, this.id);
    }

    public void delete()
    {
        this.unbind();
        GL33.glDeleteSamplers(this.id);
    }

    public void unbind(int unit)
    {
        GL33.glBindSampler(unit, 0);
        this.usedUnits.remove(unit);
    }

    public void unbind()
    {
        this.usedUnits.forEach(this::unbind);
        this.usedUnits.clear();
    }
}
