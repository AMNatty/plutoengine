package cz.tefek.pluto.engine.graphics.sprite;

public interface Sprite<T>
{
    int getX();

    int getY();

    int getWidth();

    int getHeight();

    T getSheet();
}
