package cz.tefek.pluto.tpl;

import java.nio.ByteBuffer;

public class TPNImage
{
    ByteBuffer data;
    int width;
    int height;

    public TPNImage(ByteBuffer bfr, int width, int height)
    {
        this.data = bfr;
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

    public ByteBuffer getData()
    {
        return this.data;
    }
}
