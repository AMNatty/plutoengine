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

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardInputCallback extends GLFWKeyCallback
{
    private final Set<Integer> keyPressed = new HashSet<>();
    private final Set<Integer> keyDown = new HashSet<>();
    private final Set<Integer> keyReleased = new HashSet<>();

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        if (key < 0)
        {
            return;
        }

        if (action == GLFW_PRESS)
        {
            this.keyDown.add(key);
            this.keyPressed.add(key);
        }

        if (action == GLFW_RELEASE)
        {
            this.keyDown.remove(key);
            this.keyReleased.add(key);
        }
    }

    public void resetPressed()
    {
        this.keyPressed.clear();
        this.keyReleased.clear();
    }

    public boolean hasBeenPressed(int key)
    {
        return this.keyPressed.contains(key);
    }

    public boolean hasBeenReleased(int key)
    {
        return this.keyReleased.contains(key);
    }

    public boolean isKeyDown(int key)
    {
        return this.keyDown.contains(key);
    }
}
