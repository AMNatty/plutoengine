package cz.tefek.pluto.engine.graphics.sprite;

import cz.tefek.pluto.engine.graphics.texture.MagFilter;
import cz.tefek.pluto.engine.graphics.texture.MinFilter;
import cz.tefek.pluto.engine.graphics.texture.WrapMode;
import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;

public class DisposablePlaceholderSprite extends DisposableTextureSprite
{
    public DisposablePlaceholderSprite()
    {
        super(new RectangleTexture());

        this.spriteTexture.load((String) null, MagFilter.NEAREST, MinFilter.NEAREST, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);
        this.width = this.spriteTexture.getWidth();
        this.height = this.spriteTexture.getHeight();
    }
}
