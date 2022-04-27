package cz.tefek.srclone.graphics;

import org.plutoengine.graphics.sprite.PartialTextureSprite;
import org.plutoengine.graphics.sprite.TemporalSprite;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;

import java.nio.file.Path;

public final class AnimationSprite extends TemporalSprite implements AutoCloseable
{
    private final RectangleTexture backingTexture;

    private AnimationSprite(RectangleTexture backingTexture, PartialTextureSprite[] sprite)
    {
        super(sprite);
        this.backingTexture = backingTexture;
    }

    public static AnimationSprite create(Path path, int dimensions, int width, int height, int stride)
    {
        var backingTexture = new RectangleTexture();
        backingTexture.load(path);

        var sprites = new PartialTextureSprite[dimensions];

        for (int i = 0; i < sprites.length; i++)
            sprites[i] = new PartialTextureSprite(backingTexture, i % stride * width, i / stride * height, width, height);

        return new AnimationSprite(backingTexture, sprites);
    }

    @Override
    public void close()
    {
        this.backingTexture.close();
    }
}
