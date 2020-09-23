package cz.tefek.pluto.command.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a handler for an invocation of a command.
 *
 * <p><em>
 *     Method must be public and non-static.
 * </em></p>
 *
 * <p>
 *     While classes implementing the Command API must be final, it is not required for the handler methods
 *     as that would be redundant.
 * </p>
 *
 * @author 493msi
 *
 * @since 20.2.0.0-alpha.2
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokeHandler
{
}
