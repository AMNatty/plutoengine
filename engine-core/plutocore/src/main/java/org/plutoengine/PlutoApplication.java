package org.plutoengine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.plutoengine.audio.al.AudioEngine;
import org.plutoengine.buffer.GLFWImageUtil;
import org.plutoengine.component.ComponentToken;
import org.plutoengine.display.Display;
import org.plutoengine.display.DisplayBuilder;
import org.plutoengine.input.InputBus;
import org.plutoengine.l10n.PlutoL10n;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;
import org.plutoengine.mod.ModLoader;
import org.plutoengine.util.color.IRGBA;
import org.plutoengine.util.color.RGBA;

import java.nio.file.Path;
import java.util.Locale;

/**
 * The main entry point for OpenGL applications built around the Pluto framework.
 *
 * @author 493msi
 *
 * @since pre-alpha 20.1.0
 * */
public abstract class PlutoApplication
{
    protected Display display;

    protected abstract Class<?> getMainModule();

    protected void init()
    {

    }

    protected abstract void loop();

    protected PlutoApplication()
    {

    }

    /**
     * A set of values used to create a new {@link PlutoApplication}.
     *
     * <table>
     *     <caption>Configurable options</caption>
     *     <tr><th><b>Option name</b></th><th><b>Default value</b></th><th><b>Explanation</b></th></tr>
     *     <tr>
     *         <td><code>coreProfile</code></td>
     *         <td><code>true</code></td>
     *         <td>Whether the OpenGL core profile should be enforced.</td>
     *     </tr>
     *     <tr>
     *         <td><code>majorOpenGLVersion</code></td>
     *         <td><code>3</code></td>
     *         <td>The minimum major version of the OpenGL context</td>
     *     </tr>
     *     <tr>
     *         <td><code>minorOpenGLVersion</code></td>
     *         <td><code>3</code></td>
     *         <td>The minimum minor version of the OpenGL context</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowName</code></td>
     *         <td><code>Pluto&nbsp;Engine</code></td>
     *         <td>The initial window title</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowMSAA</code></td>
     *         <td><code>4</code></td>
     *         <td>The initial MSAA</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowInitialWidth</code></td>
     *         <td><code>1280</code></td>
     *         <td>The initial window width</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowInitialHeight</code></td>
     *         <td><code>720</code></td>
     *         <td>The initial window height</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowMinWidth</code></td>
     *         <td><code>1000</code></td>
     *         <td>The minimum window width</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowMinHeight</code></td>
     *         <td><code>1000</code></td>
     *         <td>The minimum window height</td>
     *     </tr>
     *     <tr>
     *         <td><code>vsync</code></td>
     *         <td><code>0</code></td>
     *         <td>The display buffer swap interval, 0 is disabled, 1 is vsync, 2 is vsync / 2, etc.</td>
     *     </tr>
     *     <tr>
     *         <td><code>windowResizable</code></td>
     *         <td><code>true</code></td>
     *         <td>Whether the window should be resizable</td>
     *     </tr>
     *     <tr>
     *         <td><code>clearColor</code></td>
     *         <td><code>RGBA(0f, 0.7f, 1f, 0f)</code></td>
     *         <td>What color to fill the screen buffer with when beginning a new frame.</td>
     *     </tr>
     * </table>
     *
     * @author 493msi
     *
     * @since 20.2.0.0-alpha.0
     * */
    protected static class StartupConfig
    {
        private boolean coreProfile = true;
        private int majorOpenGLVersion = 3;
        private int minorOpenGLVersion = 3;
        private String windowName = "Pluto Engine";
        private int windowMSAA = 4;
        private int windowInitialWidth = 1280;
        private int windowInitialHeight = 720;
        private int windowMinWidth = 1000;
        private int windowMinHeight = 600;
        private int vsync = 0;
        private boolean windowResizable = true;
        private Path[] icons = null;
        private IRGBA clearColor = new RGBA(0f, 0.7f, 1f, 0f);

        public StartupConfig icons(Path... paths)
        {
            this.icons = paths;
            return this;
        }

        public StartupConfig coreProfile(boolean coreProfile)
        {
            this.coreProfile = coreProfile;
            return this;
        }

        public StartupConfig openGLVersion(int major, int minor)
        {
            this.majorOpenGLVersion = major;
            this.minorOpenGLVersion = minor;
            return this;
        }

        public StartupConfig windowName(String windowName)
        {
            this.windowName = windowName;
            return this;
        }

        public StartupConfig windowMSAA(int msaa)
        {
            this.windowMSAA = msaa;
            return this;
        }

        public StartupConfig windowInitialDimensions(int windowInitialWidth, int windowInitialHeight)
        {
            this.windowInitialWidth = windowInitialWidth;
            this.windowInitialHeight = windowInitialHeight;
            return this;
        }

        public StartupConfig windowMinDimensions(int windowMinWidth, int windowMinHeight)
        {
            this.windowMinWidth = windowMinWidth;
            this.windowMinHeight = windowMinHeight;
            return this;
        }

        public StartupConfig vsync(int vsyncInterval)
        {
            this.vsync = vsyncInterval;
            return this;
        }

        public StartupConfig windowResizable(boolean windowResizable)
        {
            this.windowResizable = windowResizable;
            return this;
        }

        public StartupConfig clearColor(IRGBA color)
        {
            this.clearColor = new RGBA(color.red(), color.green(), color.blue(), color.alpha());
            return this;
        }

        public StartupConfig()
        {

        }
    }

    public final void run(String[] args, StartupConfig config)
    {
        if (config == null)
        {
            config = new StartupConfig();
        }

        var globalComponents = PlutoGlobal.COMPONENTS;

        var logger = globalComponents.addComponent(Logger.TOKEN);

        Logger.log(SmartSeverity.INFO, "Debug mode: " + (Pluto.DEBUG_MODE ? "enabled" : "disabled"));

        PlutoL10n.init(Locale.UK);

        var local = PlutoLocal.instance();
        var components = local.COMPONENTS;

        DisplayBuilder.initGLFW();

        DisplayBuilder displayBuilder;

        if (config.coreProfile)
        {
            displayBuilder = new DisplayBuilder()
                    .hintOpenGLVersion(config.majorOpenGLVersion, config.minorOpenGLVersion)
                    .hintDebugContext(Pluto.DEBUG_MODE)
                    .hintMSAA(config.windowMSAA)
                    .hintVisible(true)
                    .hintResizeable(config.windowResizable)
                    .setInitialSize(config.windowInitialWidth, config.windowInitialHeight);
        }
        else
        {
            displayBuilder = new DisplayBuilder()
                    .hintOpenGLVersionLegacy(config.majorOpenGLVersion, config.minorOpenGLVersion)
                    .hintDebugContext(Pluto.DEBUG_MODE)
                    .hintMSAA(config.windowMSAA)
                    .hintVisible(true)
                    .hintResizeable(config.windowResizable)
                    .setInitialSize(config.windowInitialWidth, config.windowInitialHeight);
        }

        var displayToken = ComponentToken.create(displayBuilder::export);
        this.display = components.addComponent(displayToken);

        this.display.setName(config.windowName);

        this.display.create();

        this.display.setWindowSizeLimits(config.windowMinWidth, config.windowMinHeight, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE);

        this.display.lockSwapInterval(config.vsync);

        this.display.show();

        this.display.createOpenGLCapabilities();

        var inputBus = components.addComponent(InputBus.fromDisplay(this.display));
        var audioEngine = components.addComponent(AudioEngine.TOKEN);
        var modLoader = components.addComponent(ModLoader.with(this.getMainModule()));

        if (config.icons != null)
        {
            var icons = GLFWImageUtil.loadIconSet(config.icons);
            this.display.setIcons(icons);
        }

        this.init();

        while (!this.display.isClosing())
        {
            GL33.glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
            GL33.glClearColor(config.clearColor.red(), config.clearColor.green(), config.clearColor.blue(), config.clearColor.alpha());
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

            this.loop();

            this.display.swapBuffers();

            audioEngine.update();
            inputBus.resetStates();

            this.display.pollEvents();
        }

        components.removeComponent(audioEngine);
        components.removeComponent(modLoader);

        GL.destroy();

        components.removeComponent(inputBus);

        this.display.destroy();

        components.removeComponents(displayToken);

        DisplayBuilder.destroyGLFW();

        globalComponents.removeComponent(logger);
    }

    public Display getDisplayInstance()
    {
        return this.display;
    }
}
