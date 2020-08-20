package cz.tefek.pluto.engine.graphics.gl.fbo;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public class Framebuffer
{
    private int id;

    private List<FramebufferTexture> textures;

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
