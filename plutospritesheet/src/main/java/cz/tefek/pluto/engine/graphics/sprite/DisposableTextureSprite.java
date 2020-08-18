package cz.tefek.pluto.engine.graphics.sprite;

import cz.tefek.pluto.engine.graphics.texture.texture2d.RectangleTexture;

public class DisposableTextureSprite extends PartialTextureSprite implements SpriteDisposable<RectangleTexture>
{
    public DisposableTextureSprite(RectangleTexture texture)
    {
        super(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void delete()
    {
        this.spriteTexture.delete();
        this.spriteTexture = null;
    }
}
