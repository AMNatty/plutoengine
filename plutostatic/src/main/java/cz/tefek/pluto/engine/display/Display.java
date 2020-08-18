package cz.tefek.pluto.engine.display;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLDebugMessageARBCallback;
import org.lwjgl.system.MemoryUtil;

import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.Severity;
import cz.tefek.pluto.engine.gl.GLDebugInfo;

/**
 * A wrapper class to provide abstraction over GLFW windows.
 * 
 * @author 493msi
 * @since 0.2
 */
public class Display
{
    int width;
    int height;
    boolean debugMode;

    private boolean wasResized;

    private boolean openGLContext;

    private long windowPointer;

    private GLFWErrorCallback glfwErrorCallback;

    private GLFWWindowSizeCallback resizeCallback;

    private GLDebugMessageARBCallback glDebugCallback;

    Display()
    {
        this.glfwErrorCallback = new DisplayErrorCallback();
        GLFW.glfwSetErrorCallback(this.glfwErrorCallback);
    }

    public void create(String name)
    {
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 4);

        this.windowPointer = GLFW.glfwCreateWindow(this.width, this.height, name, MemoryUtil.NULL, MemoryUtil.NULL);

        if (this.windowPointer == MemoryUtil.NULL)
        {
            this.destroy();
            throw new IllegalStateException("Failed to create the GLFW window...");
        }

        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(this.windowPointer, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2);

        GLFW.glfwMakeContextCurrent(this.windowPointer);

        this.resizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height)
            {
                if (width > 0 && height > 0)
                {
                    if (Display.this.debugMode)
                    {
                        Logger.logf(Severity.INFO, "Resized to %dx%d.\n", width, height);
                    }

                    Display.this.width = width;
                    Display.this.height = height;
                    Display.this.wasResized = true;

                    if (Display.this.openGLContext)
                    {
                        GL33.glViewport(0, 0, Display.this.width, Display.this.height);
                    }
                }
            }
        };

        GLFW.glfwSetWindowSizeCallback(this.windowPointer, this.resizeCallback);
    }

    public void setName(String newName)
    {
        GLFW.glfwSetWindowTitle(this.windowPointer, newName);
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
        if (this.glfwErrorCallback != null)
        {
            this.glfwErrorCallback.free();
        }

        if (this.glDebugCallback != null)
        {
            this.glDebugCallback.free();
        }

        if (this.resizeCallback != null)
        {
            this.resizeCallback.free();
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
        var glCapabilities = GL.createCapabilities(true);

        GLDebugInfo.printDebugInfo(glCapabilities);

        if (this.debugMode)
        {
            this.glDebugCallback = new GLDebugMessageARBCallback() {
                @Override
                public void invoke(int source, int type, int id, int severity, int length, long message, long userParam)
                {
                    var mes = GLDebugMessageARBCallback.getMessage(length, message);
                    System.err.println(mes);
                }
            };

            ARBDebugOutput.glDebugMessageCallbackARB(this.glDebugCallback, 0);
        }

        GL33.glEnable(GL33.GL_CULL_FACE);
        GL33.glCullFace(GL33.GL_BACK);

        this.openGLContext = true;
    }
}
