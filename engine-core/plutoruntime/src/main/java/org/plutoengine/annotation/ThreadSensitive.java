package org.plutoengine.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Marks a type as a thread-sensitive unit. Accesses from other threads
 * other than the original one may result in undesirable behaviour (see below for exceptions).
 * </p>
 *
 * <p>
 * Types can opt in to set the {@link ThreadSensitive#localContexts} field to <code>true</code>,
 * committing to support per-thread local contexts.
 * </p>
 *
 * @author 493msi
 *
 * @since 20.2.0.0-alpha.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface ThreadSensitive
{
    /**
     * When <code>true</code>, the annotated type commits to support thread-local contexts.
     *
     * @since 20.2.0.0-alpha.2
     * */
    boolean localContexts() default false;
}