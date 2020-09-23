package cz.tefek.pluto.modloader.event;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.tefek.pluto.event.staticmode.StaticPlutoEvent;


/**
 * Marks a static method as an event handler for mod pre-loading.
 *
 * @author 493msi
 *
 * @since pre-alpha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@StaticPlutoEvent(passingParamClass = ModPreLoadEvent.class)
public @interface ModPreLoad
{

}
