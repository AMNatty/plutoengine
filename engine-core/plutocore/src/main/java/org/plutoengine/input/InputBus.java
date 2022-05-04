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

import org.plutoengine.component.ComponentToken;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.display.Display;

public class InputBus extends PlutoLocalComponent
{
    public static ComponentToken<InputBus> fromDisplay(Display display)
    {
        return ComponentToken.create(() -> new InputBus(display));
    }

    private final Display display;

    private Keyboard keyboard;
    private Mouse mouse;

    private InputBus(Display display)
    {
        this.display = display;
    }

    @Override
    protected void onMount(ComponentDependencyManager manager)
    {
        this.keyboard = manager.declareDependency(ComponentToken.create(() -> new Keyboard(this.display.getWindowPointer())));
        this.mouse = manager.declareDependency(ComponentToken.create(() -> new Mouse(this.display.getWindowPointer())));
    }

    public void resetStates()
    {
        this.keyboard.resetStates();
        this.mouse.resetStates();
    }

    @Override
    public boolean isUnique()
    {
        return true;
    }

}
