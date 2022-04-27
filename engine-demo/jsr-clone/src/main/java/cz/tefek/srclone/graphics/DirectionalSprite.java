package cz.tefek.srclone.graphics;

import org.plutoengine.graphics.sprite.OrientedSprite;
import org.plutoengine.graphics.sprite.PartialTextureSprite;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import java.nio.file.Path;

public final class DirectionalSprite extends OrientedSprite implements AutoCloseable
{
    private final RectangleTexture backingTexture;

    private DirectionalSprite(RectangleTexture backingTexture, PartialTextureSprite[] sprite)
    {
        super(sprite);
        this.backingTexture = backingTexture;
    }

    public static DirectionalSprite create(Path path, int dimensions, int width, int height, int stride)
    {
        var backingTexture = new RectangleTexture();
        backingTexture.load(path, MagFilter.NEAREST, MinFilter.LINEAR, WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_EDGE);

        var sprites = new PartialTextureSprite[dimensions];

        for (int i = 0; i < sprites.length; i++)
            sprites[i] = new PartialTextureSprite(backingTexture, i % stride * width, i / stride * height, width, height);

        return new DirectionalSprite(backingTexture, sprites);
    }

    @Override
    public void close()
    {
        this.backingTexture.close();
    }
}
