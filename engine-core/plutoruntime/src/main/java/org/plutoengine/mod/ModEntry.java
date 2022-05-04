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
