package org.plutoengine.graphics.sprite;

public interface SpriteDisposable<T> extends Sprite<T>, AutoCloseable
{
    @Override
    void close();
}
