package cz.tefek.pluto.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonCallback extends GLFWMouseButtonCallback
{
    public boolean[] buttonClicked = new boolean[32];
    public boolean[] buttonDown = new boolean[32];
    public boolean[] buttonReleased = new boolean[32];

    @Override
    public void invoke(long window, int button, int action, int mods)
    {
        this.buttonClicked[button] = action == GLFW.GLFW_PRESS;
        this.buttonDown[button] = action != GLFW.GLFW_RELEASE;
        this.buttonReleased[button] = action == GLFW.GLFW_RELEASE;
    }

    public void reset()
    {
        for (int i = 0; i < this.buttonClicked.length; i++)
        {
            this.buttonClicked[i] = false;
        }

        for (int i = 0; i < this.buttonClicked.length; i++)
        {
            this.buttonReleased[i] = false;
        }
    }

}
