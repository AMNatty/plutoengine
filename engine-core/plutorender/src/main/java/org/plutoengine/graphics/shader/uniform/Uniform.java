package org.plutoengine.graphics.shader.uniform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Uniform
{
    /**
     * The uniform name, corresponding to the identifier in the shader.
     */
    String name() default "";
}
