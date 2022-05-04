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

package org.plutoengine.display;

import org.lwjgl.glfw.GLFW;

public class DisplayBuilder
{
    private final Display display;

    public DisplayBuilder()
    {
        this.display = new Display();
    }

    public DisplayBuilder setInitialSize(int width, int height)
    {
        this.display.width = width;
        this.display.height = height;

        return this;
    }

    public DisplayBuilder hintResizeable(boolean resizeable)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizeable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

        return this;
    }

    public DisplayBuilder hintVisible(boolean visible)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, visible ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

        return this;
    }

    public DisplayBuilder hintMSAA(int samples)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, samples);

        return this;
    }

    public DisplayBuilder hintDebugContext(boolean enabled)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, enabled ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        this.display.debugMode = enabled;

        return this;
    }

    public DisplayBuilder hintOpenGLVersion(int major, int minor)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, major);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minor);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        this.display.coreProfile = true;

        return this;
    }

    public DisplayBuilder hintOpenGLVersionLegacy(int major, int minor)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, major);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minor);

        this.display.coreProfile = false;

        return this;
    }

    public Display export()
    {
        return this.display;
    }

    public static void initGLFW()
    {
        if (!GLFW.glfwInit())
            throw new IllegalStateException("Could not init GLFW!");
    }

    public static void destroyGLFW()
    {
        GLFW.glfwTerminate();
    }
}
