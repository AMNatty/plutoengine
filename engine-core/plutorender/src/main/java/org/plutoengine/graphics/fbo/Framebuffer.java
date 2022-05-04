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

package org.plutoengine.graphics.fbo;

import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.List;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public class Framebuffer
{
    private int id;

    private final List<FramebufferTexture> textures;

    private FramebufferDepthTexture depthTexture;

    public Framebuffer()
    {
        this.id = GL33.glGenFramebuffers();
        this.textures = new ArrayList<>(GL33.glGetInteger(GL33.GL_MAX_COLOR_ATTACHMENTS));

        Logger.logf(SmartSeverity.ADDED, "Framebuffer ID %d created.\n", this.id);
    }

    public void bind()
    {
        GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, this.id);
    }

    public void unbind()
    {
        GL33.glBindFramebuffer(GL33.GL_FRAMEBUFFER, 0);
    }

    public void delete()
    {
        GL33.glDeleteFramebuffers(this.id);
        Logger.logf(SmartSeverity.REMOVED, "Framebuffer ID %d deleted.\n", this.id);
        this.id = 0;
    }

    public void addTexture(FramebufferTexture texture)
    {
        this.bind();
        texture.bind();
        GL33.glFramebufferTexture2D(GL33.GL_FRAMEBUFFER, GL33.GL_COLOR_ATTACHMENT0
                + this.textures.size(), texture.getType(), texture.getID(), 0);
        this.textures.add(texture);
    }

    public void removeAllTextures()
    {
        this.bind();

        for (int i = this.textures.size() - 1; i > 0; --i)
        {
            var texture = this.textures.get(i);

            GL33.glFramebufferTexture2D(GL33.GL_FRAMEBUFFER, GL33.GL_COLOR_ATTACHMENT0 + i, texture.getType(), 0, 0);
        }

        this.textures.clear();
    }

    public void setDepthTexture(FramebufferDepthTexture depthTexture)
    {
        this.bind();
        depthTexture.bind();
        GL33.glFramebufferTexture2D(GL33.GL_FRAMEBUFFER, GL33.GL_DEPTH_STENCIL_ATTACHMENT, depthTexture.getType(), depthTexture.getID(), 0);
        this.depthTexture = depthTexture;
    }

    public List<FramebufferTexture> getTextures()
    {
        return this.textures;
    }

    public FramebufferDepthTexture getDepthTexture()
    {
        return this.depthTexture;
    }
}
