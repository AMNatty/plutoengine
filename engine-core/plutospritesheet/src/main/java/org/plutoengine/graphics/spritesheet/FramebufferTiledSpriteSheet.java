package org.plutoengine.graphics.spritesheet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import org.plutoengine.graphics.IRectangleShader2D;
import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.graphics.fbo.Framebuffer;
import org.plutoengine.graphics.fbo.FramebufferTexture;
import org.plutoengine.graphics.sprite.Sprite;
import org.plutoengine.graphics.sprite.TileSprite;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.sampler.Sampler2D;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;
import org.plutoengine.math.ProjectionMatrix;

public class FramebufferTiledSpriteSheet extends TiledSpriteSheet<RectangleTexture>
{
    protected Framebuffer spriteFBO;
    protected Sampler2D sampler;

    protected static IRectangleShader2D shader;

    public FramebufferTiledSpriteSheet(int tileWidth, int tileHeight)
    {
        super(tileWidth, tileHeight);
        this.spriteFBO = new Framebuffer();
        var fbTexture = new FramebufferTexture(this.getWidthInPixels(), this.getHeightInPixels(), MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
        this.spriteSheet = fbTexture;
        this.spriteFBO.addTexture(fbTexture);
        this.spriteFBO.unbind();

        this.sampler = new Sampler2D();
        this.sampler.setFilteringParameters(MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
    }

    @Override
    public void copyToNewImage()
    {
        var newSpriteSheet = new FramebufferTexture(this.getWidthInPixels(), this.getHeightInPixels(), MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);

        this.spriteFBO.bind();
        this.spriteFBO.removeAllTextures();
        this.spriteFBO.addTexture(newSpriteSheet);

        var i = 0;

        for (var sprite : this.sprites)
        {
            if (sprite == null)
            {
                i++;
                continue;
            }

            var xPos = i % this.spriteSheetWidth * this.tileWidth;
            var yPos = i / this.spriteSheetWidth * this.tileHeight;

            this.drawTileSprite(sprite, xPos, yPos, this.tileWidth, this.tileHeight);

            sprite.setX(xPos);
            sprite.setY(yPos);
            sprite.setWidth(this.tileWidth);
            sprite.setHeight(this.tileHeight);

            i++;
        }

        this.spriteFBO.unbind();
        this.spriteSheet.close();
        this.spriteSheet = newSpriteSheet;
    }

    public Framebuffer getFrameBuffer()
    {
        return this.spriteFBO;
    }

    @Override
    public void close()
    {
        this.spriteFBO.unbind();
        this.spriteFBO.delete();
        this.spriteFBO = null;

        this.sampler.delete();
        this.sampler = null;

        this.spriteSheet.close();

        super.close();
    }

    public Sampler2D getSampler()
    {
        return this.sampler;
    }

    @Override
    protected void drawTileSprite(TileSprite<TiledSpriteSheet<RectangleTexture>> sprite, int x, int y, int width, int height)
    {
        GL33.glActiveTexture(GL33.GL_TEXTURE0);

        this.spriteFBO.bind();
        GL33.glViewport(0, 0, this.getWidthInPixels(), this.getHeightInPixels());
        this.sampler.bind(0);

        GL33.glBlendFunc(GL33.GL_ONE, GL33.GL_ZERO);

        shader.start();
        shader.loadProjectionMatrix(ProjectionMatrix.createOrtho2D(this.getWidthInPixels(), this.getHeightInPixels()));

        var texture = sprite.getSheet().getSpriteSheetImage();

        RectangleRenderer2D.draw(shader).at(x, y, width, height).texture(texture, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()).flush();

        GL33.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.sampler.unbind();
        this.spriteFBO.unbind();
    }

    @Override
    protected void drawSprite(Sprite<RectangleTexture> sprite, int x, int y, int width, int height)
    {
        GL33.glActiveTexture(GL33.GL_TEXTURE0);

        this.spriteFBO.bind();
        GL33.glViewport(0, 0, this.getWidthInPixels(), this.getHeightInPixels());
        this.sampler.bind(0);

        GL33.glBlendFunc(GL33.GL_ONE, GL33.GL_ZERO);

        shader.start();
        shader.loadProjectionMatrix(ProjectionMatrix.createOrtho2D(this.getWidthInPixels(), this.getHeightInPixels()));

        RectangleRenderer2D.draw(shader).at(x, y, width, height).sprite(sprite).flush();

        GL33.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.sampler.unbind();
        this.spriteFBO.unbind();
    }

    public static void setSpriteShader(IRectangleShader2D shaderIn)
    {
        shader = shaderIn;
    }
}
