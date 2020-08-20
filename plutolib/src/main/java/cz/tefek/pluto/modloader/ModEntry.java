package cz.tefek.pluto.modloader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The heart of any Pluto mod. Annotate your class with this, so the
 * PlutoModLoader can load it. The class must be directly registered or
 * processed by {@link ModClassLoader} (as an external mod).
 *
 * @see <a href="http://pluto.tefek.cz/mod/dev/">PlutoModLoader tutorial</a> for
 *      more information.
 *
 * @author 493msi
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModEntry {
    String modid();

    String displayName() default "";

    String author() default "anonymous author";

    String version() default "unknown version";

    Class<?>[] dependencies() default {};

    String iconURL() default "";

    String description() default "No description available";

    int build() default 0;

    int earliestCompatibleBuild() default 0;

    boolean clientSupport() default true;

    boolean serverSupport() default false;
}
