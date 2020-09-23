package cz.tefek.pluto.event.staticmode;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tefek.pluto.event.EventData;

/**
 * @author 493msi
 *
 */
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface StaticPlutoEvent
{
    /**
     * This actually does nothing. ¯\_(ツ)_/¯ Well, you can use it for improved
     * code readability.
     * 
     */
    Class<?> passingParamClass() default EventData.class;
}
