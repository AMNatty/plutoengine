package org.plutoengine.audio.al;

import org.joml.Vector3fc;

import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

public record AudioSourceInfo(
    UnaryOperator<Vector3fc> moveFunction,
    BooleanSupplier keepAliveFunction
)
{
}
