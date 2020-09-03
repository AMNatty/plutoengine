package cz.tefek.pluto.engine.display;

import org.lwjgl.glfw.GLFW;

public class DisplayBuilder
{
    private Display display;

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
        {
            throw new IllegalStateException("Could not init GLFW!");
        }
    }

    public static void destroyGLFW()
    {
        GLFW.glfwTerminate();
    }
}
