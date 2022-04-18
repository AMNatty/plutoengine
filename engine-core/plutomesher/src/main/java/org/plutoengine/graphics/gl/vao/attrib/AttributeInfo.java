package org.plutoengine.graphics.gl.vao.attrib;

import org.intellij.lang.annotations.MagicConstant;
import org.plutoengine.graphics.gl.vbo.EnumArrayBufferType;

public record AttributeInfo(
    EnumArrayBufferType type,
    @MagicConstant(valuesFromClass = ReservedAttributes.class) int position,
    int dimensions
)
{
}
