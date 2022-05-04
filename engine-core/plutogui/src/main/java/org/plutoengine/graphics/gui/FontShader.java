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

package org.plutoengine.graphics.gui;

import org.joml.Matrix3fc;
import org.plutoengine.graphics.vao.attrib.ReservedAttributes;
import org.plutoengine.graphics.shader.uniform.*;
import org.plutoengine.libra.paint.LiColorPaint;
import org.plutoengine.libra.paint.LiGradientPaint;
import org.plutoengine.libra.paint.LiPaint;
import org.plutoengine.graphics.shader.ShaderBase;
import org.plutoengine.graphics.shader.ShaderProgram;
import org.plutoengine.graphics.shader.VertexArrayAttribute;
import org.plutoengine.graphics.shader.uniform.auto.AutoViewportProjection;
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
