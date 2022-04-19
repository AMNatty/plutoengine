package org.plutoengine.mod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The heart of any Pluto mod. Annotate your class with this annotation
 * so the PlutoModLoader can load it. The class must be directly registered
 * or processed by {@link ModClassLoader} (as an external mod).
 *
 * <p>
 * Mods can optionally have an entry point (e.g. {@link IModEntryPoint}.
 * </p>
 *
 * <p><b>What is a virtual mod?</b></p>
 *
 * <p>
 * Virtual mods don't have any functionality by themselves except
 * being a structural point of the mod tree. They don't have an entry point.
 * </p>
 *
 * @author 493msi
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModEntry
{
    String modID();

    String version();

    Class<?>[] dependencies() default {};
}
