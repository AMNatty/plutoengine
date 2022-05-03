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

import java.util.concurrent.TimeUnit;

public class Main extends PlutoApplication
{
    public static void main(String[] args)
    {
        var app = new Main();
        var cfg = new PlutoApplication.StartupConfig();
        cfg.windowInitialDimensions(1280, 720);
        cfg.windowName("jsr-clone");
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

        GL33.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

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
