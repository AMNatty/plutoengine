/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
