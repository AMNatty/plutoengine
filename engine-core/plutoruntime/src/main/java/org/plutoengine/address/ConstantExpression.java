package org.plutoengine.address;

import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.*;

/**
 * Denotes that the target field or method should be a constant expression - it is final, has no state
 * and always yields the same deterministic result for given input. Generally, annotated methods
 * should be thread-safe, however this is not required.
 *
 * @author 493msi
 *
 * @since 20.2.0.0-alpha.3
 * */
@TypeQualifier
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ConstantExpression
{

}
