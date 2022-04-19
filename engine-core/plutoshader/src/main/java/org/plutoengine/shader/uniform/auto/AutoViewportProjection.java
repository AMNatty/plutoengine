package org.plutoengine.shader.uniform.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a uniform to automatically receive viewport's ortographic projection.
 * 
 * @author 493msi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoViewportProjection
{

}
