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

package org.plutoengine.input.callback;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.Arrays;

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
        Arrays.fill(this.buttonClicked, false);
        Arrays.fill(this.buttonReleased, false);
    }

}
