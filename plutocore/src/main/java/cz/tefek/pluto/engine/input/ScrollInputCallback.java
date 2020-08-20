package cz.tefek.pluto.engine.input;

import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * @author 493msi
 *
 */
public class ScrollInputCallback extends GLFWScrollCallback
{
    private double xScroll;
    private double yScroll;

    @Override
    public void invoke(long window, double xoffset, double yoffset)
    {
        xScroll = xoffset;
        yScroll = yoffset;
    }

    public void reset()
    {
        xScroll = 0;
        yScroll = 0;
    }

    public double getYScroll()
    {
        return yScroll;
    }

    public void setYScroll(double yScroll)
    {
        this.yScroll = yScroll;
    }

    public double getXScroll()
    {
        return xScroll;
    }

    public void setXScroll(double xScroll)
    {
        this.xScroll = xScroll;
    }
}
