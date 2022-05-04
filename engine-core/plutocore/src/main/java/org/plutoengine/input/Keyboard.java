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
import org.plutoengine.input.callback.KeyboardCharInput;
import org.plutoengine.input.callback.KeyboardInputCallback;

public class Keyboard extends PlutoLocalComponent
{
    private final KeyboardInputCallback keyboard = new KeyboardInputCallback();
    private final KeyboardCharInput charInput = new KeyboardCharInput();

    private final long windowPointer;

    Keyboard(long windowPointer)
    {
        this.windowPointer = windowPointer;
    }

    public KeyboardInputCallback keyboard()
    {
        return this.keyboard;
    }

    public KeyboardCharInput charInput()
    {
        return this.charInput;
    }

    void resetStates()
    {
        this.keyboard.resetPressed();
        this.charInput.reset();
    }

    @Override
    protected void onMount(ComponentDependencyManager manager)
    {
        GLFW.glfwSetKeyCallback(this.windowPointer, this.keyboard);
        GLFW.glfwSetCharCallback(this.windowPointer, this.charInput);
    }

    @Override
    protected void onUnmount() throws Exception
    {
        GLFW.glfwSetKeyCallback(this.windowPointer, null);
        GLFW.glfwSetCharCallback(this.windowPointer, null);

        this.keyboard.free();
        this.charInput.free();
    }

    public boolean pressed(int key)
    {
        return this.keyboard.hasBeenPressed(key);
    }

    public boolean released(int key)
    {
        return this.keyboard.hasBeenReleased(key);
    }

    public boolean isKeyDown(int key)
    {
        return this.keyboard.isKeyDown(key);
    }

    public String getTypedText()
    {
        return this.charInput.getTypedText();
    }

    @Override
    public boolean isUnique()
    {
        return false;
    }
}
