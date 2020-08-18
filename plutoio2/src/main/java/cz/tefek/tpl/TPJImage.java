package cz.tefek.tpl;

public class TPJImage
{
    int[] data;
    int width;
    int height;

    public TPJImage(int[] pixels, int width, int height)
    {
        this.data = pixels;
        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int[] getData()
    {
        return this.data;
    }

    public int pixelAt(int x, int y)
    {
        return this.data[x + y * this.width];
    }
}
