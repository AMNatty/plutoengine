package org.plutoengine.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPositionCallback extends GLFWCursorPosCallback
{
    private double posX;
    private double posY;

    private double deltaX;
    private double deltaY;

    @Override
    public void invoke(long window, double xpos, double ypos)
    {
        this.deltaX = this.posX - xpos;
        this.deltaY = this.posY - ypos;

        this.posX = xpos;
        this.posY = ypos;
    }

    public void reset()
    {
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public double getX()
    {
        return this.posX;
    }

    public double getY()
    {
        return this.posY;
    }

    public double getDeltaX()
    {
        return this.deltaX;
    }

    public double getDeltaY()
    {
        return this.deltaY;
    }

    public boolean isInside(int x, int y, int x2, int y2)
    {
        return this.getX() > x && this.getX() < x2 && this.getY() > y && this.getY() < y2;
    }
}
