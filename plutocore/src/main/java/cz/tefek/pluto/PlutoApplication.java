package cz.tefek.pluto;

import java.util.Locale;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import cz.tefek.io.modloader.ModLoaderCore;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;
import cz.tefek.l10n.PlutoL10n;
import cz.tefek.pluto.engine.buffer.GLFWImageUtil;
import cz.tefek.pluto.engine.display.Display;
import cz.tefek.pluto.engine.display.DisplayBuilder;
import cz.tefek.pluto.engine.input.InputBus;

public abstract class PlutoApplication
{
    public static final boolean DEBUG_MODE = Boolean.valueOf(System.getProperty("cz.tefek.pluto.debug"));

    protected Display display;

    protected abstract Class<?> getMainModule();

    protected abstract void loop();

    public final void run(String[] args)
    {
        Logger.setup();

        Logger.log(SmartSeverity.INFO, "Debug mode: " + (DEBUG_MODE ? "enabled" : "disabled"));

        PlutoL10n.init(Locale.UK);

        DisplayBuilder.initGLFW();

        this.display = new DisplayBuilder().hintOpenGLVersion(3, 3).hintDebugContext(DEBUG_MODE).hintMSAA(4).hintVisible(true).hintResizeable(true).setInitialSize(1280, 720).export();

        this.display.create("Stardust Miner");

        this.display.setWindowSizeLimits(1000, 600, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);

        this.display.lockSwapInterval(0);

        this.display.show();

        var icons = GLFWImageUtil.loadIconSet("data/icon16.png", "data/icon32.png", "data/icon64.png", "data/icon128.png");

        this.display.setIcons(icons);

        this.display.createOpenGLCapabilities();

        InputBus.init(this.display);

        ModLoaderCore.registerMod(this.getMainModule());

        ModLoaderCore.loadProcedure();

        while (!this.display.isClosing())
        {
            GL33.glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
            GL33.glClearColor(0f, 0.7f, 1f, 0f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

            this.loop();

            this.display.swapBuffers();

            InputBus.resetStates();

            this.display.pollEvents();
        }

        InputBus.destroy();

        ModLoaderCore.unloadProcedure();

        GL.destroy();

        this.display.destroy();

        DisplayBuilder.destroyGLFW();
    }

    public Display getDisplayInstance()
    {
        return this.display;
    }
}
