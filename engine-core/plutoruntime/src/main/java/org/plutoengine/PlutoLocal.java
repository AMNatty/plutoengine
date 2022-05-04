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

package org.plutoengine;

import org.plutoengine.annotation.ThreadSensitive;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.component.ComponentManager;

/**
 * @since 20.2.0.0-alpha.3
 *
 * @author 493msi
 */
@ThreadSensitive(localContexts = true)
public class PlutoLocal
{
    private static final ThreadLocal<PlutoLocal> local = ThreadLocal.withInitial(PlutoLocal::new);

    public final ComponentManager<PlutoLocalComponent> COMPONENTS;

    private PlutoLocal()
    {
        this.COMPONENTS = new ComponentManager<>(PlutoLocalComponent.class);
    }

    public static PlutoLocal instance()
    {
        return local.get();
    }

    public static ComponentManager<PlutoLocalComponent> components()
    {
        return instance().COMPONENTS;
    }
}
