package org.plutoengine.graphics.gui;

import org.joml.Matrix3fc;
import org.plutoengine.graphics.gl.vao.attrib.ReservedAttributes;
import org.plutoengine.libra.paint.LiColorPaint;
import org.plutoengine.libra.paint.LiGradientPaint;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.shader.ShaderBase;
import org.plutoengine.shader.ShaderProgram;
import org.plutoengine.shader.VertexArrayAttribute;
import org.plutoengine.shader.uniform.*;
import org.plutoengine.shader.uniform.auto.AutoViewportProjection;
import org.plutoengine.util.color.IRGBA;

@ShaderProgram
public final class FontShader extends ShaderBase implements ISDFTextShader
{
    @AutoViewportProjection
    @Uniform(name = "projection")
    public UniformMat4 projectionMatrix;

    @Uniform(name = "transformation")
    public UniformMat3 transformationMatrix;

    @Uniform
    public UniformInt paintType;

    @Uniform
    public UniformRGBA paintColor;

    @Uniform
    public UniformInt paintGradientStopCount;

    @Uniform
    public UniformArrayRGBA paintGradientColors;

    @Uniform
    public UniformArrayFloat paintGradientPositions;

    @Uniform
    public UniformArrayVec2 paintGradientEnds;

    @Uniform
    public UniformFloat pxScale;

    @VertexArrayAttribute(ReservedAttributes.POSITION)
    public int position;

    @VertexArrayAttribute(ReservedAttributes.UV)
    public int uvCoords;

    @VertexArrayAttribute(2)
    public int page;

    @VertexArrayAttribute(3)
    public int paintUVCoords;

    @Override
    public void setTransform(Matrix3fc transform)
    {
        this.transformationMatrix.load(transform);
    }

    @Override
    public void setPaint(LiPaint paint)
    {
        switch (paint.getType())
        {
            case SOLID_COLOR -> {
                var col = ((LiColorPaint) paint).getColor();
                this.paintType.load(0);
                this.paintColor.load(col.getFloatComponentsRGBA());
            }

            case GRADIENT -> {
                var gradPaint = (LiGradientPaint) paint;
                this.paintType.load(1);
                this.paintGradientEnds.load(gradPaint.getStart(), gradPaint.getEnd());
                var stops = gradPaint.getStops();
                this.paintGradientStopCount.load(stops.length);
                var colors = new IRGBA[stops.length];
                var positions = new float[stops.length];

                int i = 0;
                for (var stop : stops)
                {
                    var col = stop.color();

                    colors[i] = col.getFloatComponentsRGBA();
                    positions[i] = stop.position();
                    i++;
                }

                this.paintGradientColors.load(colors);
                this.paintGradientPositions.load(positions);
            }
        }
    }

    @Override
    public void setPixelScale(float scale)
    {
        this.pxScale.load(scale);
    }
}
