package cz.tefek.srclone.particle;

import org.plutoengine.graphics.ImmediateFontRenderer;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.util.color.Color;
import org.plutoengine.util.color.RGBA;

import cz.tefek.srclone.SRCloneMod;

public class ParticleText extends Particle
{
    protected String text;

    public ParticleText(String text)
    {
        this.text = text;
        this.finalScale = 2.0f;
        this.size = 16.0f;
        this.initialLifetime = 2.0f;
    }

    @Override
    public void render()
    {
        var col = new RGBA(1.0f, 1.0f, 1.0f, Math.min(1.0f, 3.0f - 4.0f * this.animationTimer));

        var style = new TextStyleOptions(this.scale * this.size)
            .setPaint(LiPaint.solidColor(Color.from(col)))
            .setVerticalAlign(TextStyleOptions.TextAlign.CENTER)
            .setHorizontalAlign(TextStyleOptions.TextAlign.CENTER);

        ImmediateFontRenderer.drawString(this.getRenderX(), this.getRenderY(), this.text, SRCloneMod.srCloneFont, style);
    }
}
