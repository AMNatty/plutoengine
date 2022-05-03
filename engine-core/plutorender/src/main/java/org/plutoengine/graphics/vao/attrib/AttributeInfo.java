package org.plutoengine.graphics.vao.attrib;

import org.intellij.lang.annotations.MagicConstant;
import org.plutoengine.graphics.vbo.EnumArrayBufferType;

public record AttributeInfo(
    EnumArrayBufferType type,
    @MagicConstant(valuesFromClass = ReservedAttributes.class) int position,
    int dimensions
)
{
}
