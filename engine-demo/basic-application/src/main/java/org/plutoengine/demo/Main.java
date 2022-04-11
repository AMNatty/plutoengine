package org.plutoengine.demo;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import org.plutoengine.PlutoApplication;
import org.plutoengine.display.Display;
import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.Renderer2D;
import org.plutoengine.graphics.TestFontRenderer;
import org.plutoengine.graphics.gl.vao.QuadPresets;
import org.plutoengine.graphics.gui.FontShader;
import org.plutoengine.graphics.texture.MagFilter;
import org.plutoengine.graphics.texture.MinFilter;
import org.plutoengine.graphics.texture.WrapMode;
import org.plutoengine.graphics.texture.texture2d.Texture2D;
import org.plutoengine.input.InputBus;
import org.plutoengine.math.ProjectionMatrix;
import org.plutoengine.shader.RenderShaderBuilder;
import org.plutoengine.shader.uniform.auto.AutomaticUniforms;
import org.plutoengine.tpl.ImageLoader;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;

public class Main extends PlutoApplication
{
    public static Main INSTANCE;

    public static void main(String[] args) throws Exception
    {
        var config = new PlutoApplication.StartupConfig();
        config.vsync(1);
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

        TestFontRenderer.drawString(BasicApplicationDemoMod.font, "Testing ");

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

                newShader.start();
                newShader.recolor.load(1, 1, 1, 1);
            }
        }

        /*
        var va = Renderer2D.standardQuad;
        va.bind();
        va.enableAllAttributes();

        var font = BasicApplicationDemoMod.font;
        var tex = font.getAtlas();

        tex.bind();

        var shader = PlutoGUIMod.fontShader;

        shader.start();
        shader.recolor.load(1, 1, 1, 1);
        shader.italic.load(false);
        shader.page.load(0);
        shader.uvBase.load(0.0f, 0.0f);
        shader.uvDelta.load(1.0f, 1.0f);

        for (int i = 0; i < tex.getDepth(); i++)
        {
            var padding = 8;
            var x = i % 3;
            var y = i / 3;
            var size = this.size;

            shader.transformationMatrix.load(TransformationMatrix.create(20 + x * (size + padding), 20 + y * (size + padding), 0,
                0, 0, 0,
                size, size, 1));

            shader.page.load(i);
            va.draw(DrawMode.TRIANGLES);
        }

        this.size += InputBus.Mouse.getScrollY() * 20;

        if (InputBus.Keyboard.pressed(GLFW.GLFW_KEY_R))
        {
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

         */

        /*
        var buildStr = String.format("Build %s", VersionInfo.GAME_BUILD);
        var strWidth = FontHelper.calcStringWidth(buildStr, "default", 0.75f);
        FontRenderer.drawString(this.display.getWidth() - strWidth + 1, 3, buildStr, 0, 0, 0, 1, 0.75f, true);
        FontRenderer.drawString(this.display.getWidth() - strWidth, 2, buildStr, 0.7f, 0.7f, 0.7f, 1, 0.75f, false);

        var fpsStr = String.format("%d FPS", Framerate.getInterpolatedFPS());
        FontRenderer.drawString(3, 3, fpsStr, 0, 0, 0, 1, 0.75f, true);
        FontRenderer.drawString(2, 2, fpsStr, 0.13f, 0.75f, 0.62f, 1, 0.75f, false);

        var mods = PlutoLocal.components().getComponent(ModLoader.class).getAllMods();
        int modNr = 0;

        for (var mod : mods)
        {
            var modManifest = mod.getManifest();
            var modStr = String.format("%s &c[0xff999999]&i1%s", modManifest.displayName(), mod.getVersion());

            FontRenderer.drawString(8, 50 + modNr * 18, modStr, 0, 0, 0, 0, 0.7f, "default", true);
            FontRenderer.drawString(7, 49 + modNr * 18, modStr, 1, 1, 1, 1, 0.7f, "default", false);

            modNr++;
        }

         */
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
