package org.plutoengine.demo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import org.plutoengine.PlutoApplication;
import org.plutoengine.display.Display;
import org.plutoengine.display.Framerate;
import org.plutoengine.gui.font.FontHelper;
import org.plutoengine.gui.font.FontRenderer;
import org.plutoengine.math.ProjectionMatrix;
import org.plutoengine.shader.uniform.auto.AutomaticUniforms;

public class Main extends PlutoApplication
{
    public static Main INSTANCE;

    public static void main(String[] args) throws Exception
    {
        var config = new PlutoApplication.StartupConfig();
        config.windowName("JSRClone " + VersionInfo.GAME_VERSION);

        INSTANCE = new Main();
        INSTANCE.run(args, config);
    }

    @Override
    public void loop()
    {
        GL33.glEnable(GL11.GL_BLEND);
        GL33.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        var projection = ProjectionMatrix.createOrtho2D(this.display.getWidth(), this.display.getHeight());
        AutomaticUniforms.VIEWPORT_PROJECTION.fire(projection);

        var buildStr = String.format("Build %s", VersionInfo.GAME_BUILD);
        var strWidth = FontHelper.calcStringWidth(buildStr, "default", 0.75f);
        FontRenderer.drawString(this.display.getWidth() - strWidth + 1, 3, buildStr, 0, 0, 0, 1, 0.75f, true);
        FontRenderer.drawString(this.display.getWidth() - strWidth, 2, buildStr, 0.7f, 0.7f, 0.7f, 1, 0.75f, false);

        var fpsStr = String.format("%d FPS", Framerate.getInterpolatedFPS());
        FontRenderer.drawString(3, 3, fpsStr, 0, 0, 0, 1, 0.75f, true);
        FontRenderer.drawString(2, 2, fpsStr, 0.13f, 0.75f, 0.62f, 1, 0.75f, false);
    }

    public static Display getDisplay()
    {
        return INSTANCE.getDisplayInstance();
    }

    @Override
    protected Class<?> getMainModule()
    {
        return BasicApplicationDemoMod.class;
    }
}
