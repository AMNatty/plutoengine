package org.plutoengine.input;

import org.lwjgl.glfw.GLFW;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.input.callback.CursorPositionCallback;
import org.plutoengine.input.callback.MouseButtonCallback;
import org.plutoengine.input.callback.ScrollInputCallback;

public class Mouse extends PlutoLocalComponent
{
    private final MouseButtonCallback mouseButton = new MouseButtonCallback();
    private final CursorPositionCallback cursorPosition = new CursorPositionCallback();
    private final ScrollInputCallback scroll = new ScrollInputCallback();

    private final long windowPointer;

    Mouse(long windowPointer)
    {
        this.windowPointer = windowPointer;
    }

    public MouseButtonCallback mouseButtons()
    {
        return this.mouseButton;
    }

    public ScrollInputCallback scroll()
    {
        return this.scroll;
    }

    public CursorPositionCallback cursorPosition()
    {
        return this.cursorPosition;
    }

    void resetStates()
    {
        this.mouseButton.reset();
        this.scroll.reset();
        this.cursorPosition.reset();
    }

    @Override
    protected void onMount(ComponentDependencyManager manager)
    {
        GLFW.glfwSetMouseButtonCallback(this.windowPointer, this.mouseButton);
        GLFW.glfwSetCursorPosCallback(this.windowPointer, this.cursorPosition);
        GLFW.glfwSetScrollCallback(this.windowPointer, this.scroll);
    }

    @Override
    protected void onUnmount() throws Exception
    {
        GLFW.glfwSetMouseButtonCallback(this.windowPointer, null);
        GLFW.glfwSetCursorPosCallback(this.windowPointer, null);
        GLFW.glfwSetScrollCallback(this.windowPointer, null);

        this.mouseButton.free();
        this.cursorPosition.free();
        this.scroll.free();
    }

    public boolean clicked(int button)
    {
        return this.mouseButton.buttonClicked[button];
    }

    public boolean released(int button)
    {
        return this.mouseButton.buttonReleased[button];
    }

    public boolean isButtonDown(int button)
    {
        return this.mouseButton.buttonDown[button];
    }

    public double getX()
    {
        return this.cursorPosition.getX();
    }

    public double getY()
    {
        return this.cursorPosition.getY();
    }

    public boolean isInside(int x1, int y1, int x2, int y2)
    {
        return this.cursorPosition.isInside(x1, y1, x2, y2);
    }

    public double getDX()
    {
        return this.cursorPosition.getDeltaX();
    }

    public double getDY()
    {
        return this.cursorPosition.getDeltaY();
    }

    public double getScrollX()
    {
        return this.scroll.getXScroll();
    }

    public double getScrollY()
    {
        return this.scroll.getYScroll();
    }

    @Override
    public boolean isUnique()
    {
        return false;
    }
}
