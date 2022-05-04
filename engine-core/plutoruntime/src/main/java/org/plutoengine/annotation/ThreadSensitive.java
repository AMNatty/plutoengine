/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
