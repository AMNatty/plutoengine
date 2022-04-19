package org.plutoengine.graphics.sprite;

import java.awt.image.BufferedImage;

import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

public class DisposablePlaceholderSprite extends DisposableTextureSprite
{
    public DisposablePlaceholderSprite()
    {
        super(new RectangleTexture());

        this.spriteTexture.load((BufferedImage) null, MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
        this.width = this.spriteTexture.getWidth();
        this.height = this.spriteTexture.getHeight();
    }
}
