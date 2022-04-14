package org.plutoengine.graphics.gui;

import org.joml.Matrix3fc;
import org.plutoengine.libra.paint.LiPaint;

public interface IGUIShader
{
    void setTransform(Matrix3fc transform);

    void setPaint(LiPaint paint);
}
