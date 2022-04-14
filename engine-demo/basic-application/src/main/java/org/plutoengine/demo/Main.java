package org.plutoengine.demo;

import org.joml.primitives.Rectanglef;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.plutoengine.Pluto;
import org.plutoengine.PlutoApplication;
import org.plutoengine.PlutoLocal;
import org.plutoengine.display.Display;
import org.plutoengine.display.Framerate;
import org.plutoengine.graphics.ImmediateFontRenderer;
import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.RectangleRenderer2D;
import org.plutoengine.graphics.gui.FontShader;
import org.plutoengine.input.InputBus;
import org.plutoengine.libra.paint.LiGradientPaint;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.math.ProjectionMatrix;
import org.plutoengine.mod.ModLoader;
import org.plutoengine.shader.RenderShaderBuilder;
import org.plutoengine.shader.uniform.auto.AutomaticUniforms;
import org.plutoengine.util.color.Color;
import org.plutoengine.util.color.EnumColorFormat;
import org.plutoengine.util.color.HSB;

import java.util.stream.Collectors;

public class Main extends PlutoApplication
{
    public static Main INSTANCE;

    public static void main(String[] args)
    {
        var config = new PlutoApplication.StartupConfig();
        config.vsync(1);
        config.windowName("PlutoEngine Demo " + VersionInfo.GAME_VERSION);

        INSTANCE = new Main();
        INSTANCE.run(args, config);
    }

    @Override
    public void loop()
    {
        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

        var projection = ProjectionMatrix.createOrtho2D(this.display.getWidth(), this.display.getHeight());
        AutomaticUniforms.VIEWPORT_PROJECTION.fire(projection);

        var style = new TextStyleOptions(20.0f);
        style.setPaint(LiPaint.solidColor(Color.WHITE));

        var mods = PlutoLocal.components().getComponent(ModLoader.class).getAllMods();

        var modStr = mods.stream().map(mod -> {
            var modManifest = mod.getManifest();
            return String.format("%s %s", modManifest.displayName(), mod.getVersion());
        }).collect(Collectors.joining("\n"));

        ImmediateFontRenderer.drawString(5, 5, Framerate.getInterpolatedFPS() + " FPS", BasicApplicationDemoMod.font, style);
        ImmediateFontRenderer.drawString(5, 30, modStr, BasicApplicationDemoMod.font, style);

        var watermarkStyle = new TextStyleOptions(20.0f);
        watermarkStyle.setHorizontalAlign(TextStyleOptions.TextAlign.CENTER);
        watermarkStyle.setPaint(LiPaint.solidColor(Color.from(0x4f000000, EnumColorFormat.CF_INT_ARGB)));
        ImmediateFontRenderer.drawString(this.display.getWidth() / 2.0f, 5, "PlutoEngine demo build licensed the MIT license...", BasicApplicationDemoMod.font, watermarkStyle);

        RectangleRenderer2D.draw()
                           .at(this.display.getWidth() / 2.0f - 150, this.display.getHeight() / 2.0f - 246, 300, 300)
                           .recolor(0.5f, 0.5f, 0.5f, 1.0f)
                           .texture(BasicApplicationDemoMod.plutoLogo).flush();

        RectangleRenderer2D.draw()
                           .at(this.display.getWidth() / 2.0f - 150, this.display.getHeight() / 2.0f - 250, 300, 300)
                           .texture(BasicApplicationDemoMod.plutoLogo).flush();

        var welcomeStyle = new TextStyleOptions(60.0f)
            .setHorizontalAlign(TextStyleOptions.TextAlign.CENTER)
            .setVerticalAlign(TextStyleOptions.TextAlign.CENTER)
            .setOverflowX(TextStyleOptions.OverflowXStrategy.SCALE_TO_FIT)
            .setFitBox(new Rectanglef(50.0f, 50.0f, this.display.getWidth() - 50.0f, this.display.getHeight() - 50.0f))
            .setPaint(LiPaint.solidColor(Color.VERY_DARK_GRAY));
        ImmediateFontRenderer.drawString(0, 102, "Welcome to PlutoEngine v. %s!".formatted(Pluto.VERSION), BasicApplicationDemoMod.font, welcomeStyle);
        float gPos = (System.currentTimeMillis() % 7200) / 20.0f;
        ImmediateFontRenderer.drawString(500, 50, "Hue: %f".formatted(gPos), BasicApplicationDemoMod.font, style);

        var stops = new LiGradientPaint.Stop[16];
        for (int i = 0; i < stops.length; i++)
            stops[i] = new LiGradientPaint.Stop(i / (float) stops.length, Color.from(new HSB(gPos + i * 10, 1.0f, 1.0f).toRGBA()));

        welcomeStyle.setPaint(LiPaint.horizontaLinearGradient(stops));
        ImmediateFontRenderer.drawString(0, 100, "Welcome to PlutoEngine v. %s!".formatted(Pluto.VERSION), BasicApplicationDemoMod.font, welcomeStyle);

        if (InputBus.Keyboard.pressed(GLFW.GLFW_KEY_R))
        {
            var shader = PlutoGUIMod.fontShader;

            var newShader = new RenderShaderBuilder(
                PlutoGUIMod.instance.getResource("shaders.VertexFontShader#glsl"),
                PlutoGUIMod.instance.getResource("shaders.FragmentFontShader#glsl")
            ).build(FontShader.class, false);

            if (newShader != null)
            {
                shader.stop();
                shader.close();
                PlutoGUIMod.fontShader = newShader;
            }
        }
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
