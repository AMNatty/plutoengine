package org.plutoengine.graphics.sprite;

import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

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
