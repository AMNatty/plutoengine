package cz.tefek.pluto.modloader.event;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tefek.pluto.event.staticmode.StaticPlutoEvent;

@Retention(RUNTIME)
@Target(METHOD)
@StaticPlutoEvent(passingParamClass = ModUnloadEvent.class)
/**
 * @author 493msi
 *
 */
public @interface ModUnload {

}
