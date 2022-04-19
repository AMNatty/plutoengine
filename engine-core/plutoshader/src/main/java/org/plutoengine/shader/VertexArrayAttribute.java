package org.plutoengine.shader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VertexArrayAttribute
{
    /**
     * The attribute ID.
     */
    int value();

    /**
     * The attribute name, corresponding to the identifier in the shader.
     */
    String name() default "";
}
