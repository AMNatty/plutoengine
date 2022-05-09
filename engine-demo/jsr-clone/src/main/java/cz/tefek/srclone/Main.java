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

package cz.tefek.srclone;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.plutoengine.PlutoApplication;
import org.plutoengine.PlutoLocal;
import org.plutoengine.display.Framerate;
import org.plutoengine.graphics.ImmediateFontRenderer;
import org.plutoengine.input.Keyboard;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.math.ProjectionMatrix;
import org.plutoengine.graphics.shader.uniform.auto.AutomaticUniforms;
import org.plutoengine.util.color.Color;
import org.plutoengine.util.color.RGBA;

import java.util.concurrent.TimeUnit;

public class Main extends PlutoApplication
{
    public static void main(String[] args)
    {
        var app = new Main();
        var cfg = new PlutoApplication.StartupConfig();
        cfg.windowInitialDimensions(1280, 720);
        cfg.windowName("jsr-clone");
        cfg.clearColor(new RGBA(0.0f, 0.0f, 0.0f, 1.0f));
        // cfg.vsync(1);
        app.run(args, cfg);
    }

    private Game game;

    @Override
    protected Class<?> getMainModule()
    {
        return SRCloneMod.class;
    }

    @Override
    protected void init()
    {
        this.game = new Game();
    }

    @Override
    protected void loop()
    {
        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

        var projection = ProjectionMatrix.createOrtho2D(this.display.getWidth(), this.display.getHeight());
        AutomaticUniforms.VIEWPORT_PROJECTION.fire(projection);

        var keyboard = PlutoLocal.components().getComponent(Keyboard.class);
        if (this.game.isOver() && keyboard.pressed(GLFW.GLFW_KEY_R))
        {
            this.game = new Game();
        }

        var delta = Framerate.getFrameTime() / TimeUnit.SECONDS.toMillis(1);

        this.game.tick(delta);

        ImmediateFontRenderer.drawString(this.display.getWidth(), 5, Framerate.getInterpolatedFPS() + " FPS", SRCloneMod.font, new TextStyleOptions(32).setPaint(LiPaint.solidColor(Color.WHITE)).setHorizontalAlign(TextStyleOptions.TextAlign.END));
    }
}
