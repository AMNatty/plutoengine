package org.plutoengine.shader.uniform.auto;

import org.joml.Matrix4fc;

import org.plutoengine.event.lambda.LambdaEventFactory;
import org.plutoengine.event.lambda.LambdaEventFactory.LambdaEvent;

public class AutomaticUniforms
{
    public static final LambdaEvent<Matrix4fc> VIEWPORT_PROJECTION = LambdaEventFactory.createEvent();
}
