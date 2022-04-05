package org.plutoengine.display;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import org.plutoengine.Pluto;
import org.plutoengine.address.ThreadSensitive;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.gl.GLDebugInfo;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

/**
 * A wrapper class to provide abstraction over GLFW windows.
 * 
 * @author 493msi
 * @since 0.2
 */
@ThreadSensitive(localContexts = true)
public class Display extends PlutoLocalComponent
{
    int width;
    int height;
    boolean debugMode;
    boolean coreProfile = true;

    private String name = Pluto.ENGINE_NAME;

    private boolean wasResized;

    private boolean openGLContext;

    private long windowPointer;

    private final GLFWErrorCallbackI glfwErrorCallback;

    private GLFWWindowSizeCallbackI resizeCallback;

    private GLDebugMessageARBCallbackI glDebugCallback;

    Display()
    {
        this.windowPointer = MemoryUtil.NULL;

        this.glfwErrorCallback = (int error, long description) -> {
            Logger.logf(SmartSeverity.ERROR, "GLFW Error code %d:\n", error);
            Logger.logf(GLFWErrorCallback.getDescription(description));
        };

        GLFW.glfwSetErrorCallback(this.glfwErrorCallback);
    }

    public void create()
    {
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 4);

        this.windowPointer = GLFW.glfwCreateWindow(this.width, this.height, this.name, MemoryUtil.NULL, MemoryUtil.NULL);

        if (this.windowPointer == MemoryUtil.NULL)
        {
            this.destroy();
            throw new IllegalStateException("Failed to create a window...");
        }

        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        if (vidmode == null)
        {
            this.destroy();
            throw new IllegalStateException("Failed to detect the primary monitor.");
        }

        GLFW.glfwSetWindowPos(this.windowPointer, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);

        GLFW.glfwMakeContextCurrent(this.windowPointer);

        this.resizeCallback = (long window, int width, int height) -> {
            if (width > 0 && height > 0)
            {
                if (this.debugMode)
                {
                    Logger.logf(SmartSeverity.INFO, "Resized to %dx%d.\n", width, height);
                }

                this.width = width;
                this.height = height;
                this.wasResized = true;

                if (this.openGLContext)
                {
                    GL33.glViewport(0, 0, this.width, this.height);
                }
            }
        };

        GLFW.glfwSetWindowSizeCallback(this.windowPointer, this.resizeCallback);
    }

    public void setName(String newName)
    {
        this.name = newName;

        if (this.windowPointer != MemoryUtil.NULL)
            GLFW.glfwSetWindowTitle(this.windowPointer, this.name);
    }

    public void show()
    {
        GLFW.glfwShowWindow(this.windowPointer);
    }

    public void setWindowSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight)
    {
        GLFW.glfwSetWindowSizeLimits(this.windowPointer, minWidth, minHeight, maxWidth, maxHeight);
    }

    public void setIcons(GLFWImage.Buffer iconBuffer)
    {
        GLFW.glfwSetWindowIcon(this.windowPointer, iconBuffer);

        iconBuffer.flip();
        iconBuffer.forEach(GLFWImage::free);
        iconBuffer.free();
    }

    public void setShouldClose(boolean shouldClose)
    {
        GLFW.glfwSetWindowShouldClose(this.windowPointer, shouldClose);
    }

    public void maximize()
    {
        GLFW.glfwMaximizeWindow(this.windowPointer);
    }

    public void lockSwapInterval(int interval)
    {
        GLFW.glfwSwapInterval(interval);
    }

    public void swapBuffers()
    {
        GLFW.glfwSwapBuffers(this.windowPointer);
        Framerate.tick();
    }

    public boolean isClosing()
    {
        return GLFW.glfwWindowShouldClose(this.windowPointer);
    }

    public void pollEvents()
    {
        this.wasResized = false;
        GLFW.glfwPollEvents();
    }

    public void destroy()
    {
        if (this.glfwErrorCallback instanceof GLFWErrorCallback glfwErrorCallback)
        {
            glfwErrorCallback.free();
        }

        if (this.glDebugCallback instanceof GLDebugMessageARBCallback glDebugMessageARBCallback)
        {
            glDebugMessageARBCallback.free();
        }

        if (this.resizeCallback instanceof GLFWWindowSizeCallback windowSizeCallback)
        {
            windowSizeCallback.free();
        }

        if (this.windowPointer != MemoryUtil.NULL)
        {
            GLFW.glfwDestroyWindow(this.windowPointer);
        }
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public long getWindowPointer()
    {
        return this.windowPointer;
    }

    public boolean wasResized()
    {
        return this.wasResized;
    }

    public void createOpenGLCapabilities()
    {
        var glCapabilities = GL.createCapabilities(this.coreProfile);

        GLDebugInfo.printDebugInfo(glCapabilities);

        if (this.debugMode)
        {
            this.glDebugCallback = (int source, int type, int id, int severity, int length, long messagePtr, long userParam) -> {
                var message = GLDebugMessageARBCallback.getMessage(length, messagePtr);
                Logger.log(SmartSeverity.WARNING, message);
            };

            ARBDebugOutput.glDebugMessageCallbackARB(this.glDebugCallback, MemoryUtil.NULL);
        }

        GL33.glEnable(GL33.GL_CULL_FACE);
        GL33.glCullFace(GL33.GL_BACK);

        this.openGLContext = true;
    }

    @Override
    public boolean isUnique()
    {
        return true;
    }
}
