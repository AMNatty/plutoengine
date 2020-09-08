package cz.tefek.pluto;

import java.io.IOException;
import java.util.Locale;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import cz.tefek.pluto.engine.audio.al.AudioEngine;
import cz.tefek.pluto.engine.buffer.GLFWImageUtil;
import cz.tefek.pluto.engine.display.Display;
import cz.tefek.pluto.engine.display.DisplayBuilder;
import cz.tefek.pluto.engine.input.InputBus;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;
import cz.tefek.pluto.l10n.PlutoL10n;
import cz.tefek.pluto.modloader.ModLoaderCore;

public abstract class PlutoApplication
{
    protected Display display;

    protected abstract Class<?> getMainModule();

    protected abstract void loop();

    protected static class StartupConfig
    {
        public boolean coreProfile = true;
        public int majorOpenGLVersion = 3;
        public int minorOpenGLVersion = 3;
        public String windowName = "Pluto Engine";
        public int windowMSAA = 4;
        public int windowInitialWidth = 1280;
        public int windowInitialHeight = 720;
        public int windowMinWidth = 1000;
        public int windowMinHeight = 600;
        public int vsync = 0;
        public boolean windowResizable = true;

        public StartupConfig()
        {

        }
    }

    public final void run(String[] args, StartupConfig config) throws Exception
    {
        if (config == null)
        {
            config = new StartupConfig();
        }

        Logger.setup();

        Logger.log(SmartSeverity.INFO, "Debug mode: " + (Pluto.DEBUG_MODE ? "enabled" : "disabled"));

        PlutoL10n.init(Locale.UK);

        DisplayBuilder.initGLFW();

        if (config.coreProfile)
        {
            this.display = new DisplayBuilder().hintOpenGLVersion(config.majorOpenGLVersion, config.minorOpenGLVersion).hintDebugContext(Pluto.DEBUG_MODE).hintMSAA(config.windowMSAA).hintVisible(true).hintResizeable(config.windowResizable).setInitialSize(config.windowInitialWidth, config.windowInitialHeight).export();
        }
        else
        {
            this.display = new DisplayBuilder().hintOpenGLVersionLegacy(config.majorOpenGLVersion, config.minorOpenGLVersion).hintDebugContext(Pluto.DEBUG_MODE).hintMSAA(config.windowMSAA).hintVisible(true).hintResizeable(config.windowResizable).setInitialSize(config.windowInitialWidth, config.windowInitialHeight).export();
        }

        this.display.create(config.windowName);

        this.display.setWindowSizeLimits(config.windowMinWidth, config.windowMinHeight, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);

        this.display.lockSwapInterval(config.vsync);

        this.display.show();

        // TODO Un-hardcode these
        var icons = GLFWImageUtil.loadIconSet("data/icon16.png", "data/icon32.png", "data/icon64.png", "data/icon128.png");

        this.display.setIcons(icons);

        this.display.createOpenGLCapabilities();

        InputBus.init(this.display);

        AudioEngine.initialize();

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

        AudioEngine.exit();

        InputBus.destroy();

        ModLoaderCore.unloadProcedure();

        GL.destroy();

        this.display.destroy();

        DisplayBuilder.destroyGLFW();

        Logger.close();
    }

    public Display getDisplayInstance()
    {
        return this.display;
    }
}
