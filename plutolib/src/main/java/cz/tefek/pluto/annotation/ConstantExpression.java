package cz.tefek.pluto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes that the target field or method should be a constant expression - it has no state and always yields
 * the same deterministic result for given input. Generally, annotated methods should be thread-safe, however
 * this is not required.
 *
 * @author 493msi
 *
 * @since 20.2.0.0-alpha.2
 * */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ConstantExpression
{
}
