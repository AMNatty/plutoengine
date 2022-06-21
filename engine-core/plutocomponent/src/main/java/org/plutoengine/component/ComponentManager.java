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

package org.plutoengine.component;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class ComponentManager<R extends AbstractComponent<R>>
{
    private final Class<R> base;

    protected final MultiValuedMap<ComponentToken<?>, R> components;
    protected final Map<R, ComponentToken<?>> tokens;
    protected final MultiValuedMap<Class<?>, R> implementationProviders;
    protected final MultiValuedMap<R, Class<?>> implementationReceivers;

    public ComponentManager(@NotNull Class<R> base)
    {
        this.base = base;
        this.components = new HashSetValuedHashMap<>();
        this.tokens = new HashMap<>();
        this.implementationProviders = new HashSetValuedHashMap<>();
        this.implementationReceivers = new ArrayListValuedHashMap<>();
    }

    public <T extends R> T addComponent(@NotNull ComponentToken<T> token)
    {
        T component = token.createInstance();
        var clazz = component.getClass();

        if (component.isUnique() && this.implementationProviders.containsKey(clazz))
            throw new IllegalArgumentException("Cannot have two components of the same class '%s'".formatted(clazz.getCanonicalName()));

        var superclasses = ClassUtils.getAllSuperclasses(clazz);

        for (var superclass : superclasses)
        {
            if (superclass.isAssignableFrom(AbstractComponent.class))
                continue;

            this.implementationProviders.put(superclass, component);
            this.implementationReceivers.put(component, superclass);
        }

        this.implementationProviders.put(clazz, component);
        this.components.put(token, component);
        this.tokens.put(component, token);

        this.onComponentAdded(component);

        return component;
    }

    @MustBeInvokedByOverriders
    protected void onComponentAdded(R component)
    {
        try
        {
            component.initialize(this);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An exception has occured while mounting the component", e);
        }
    }

    public Class<R> getComponentBase()
    {
        return this.base;
    }

    public <T extends R> Stream<T> streamComponents(@NotNull Class<T> componentClazz)
    {
        var providers = this.implementationProviders.get(componentClazz);

        return providers.stream().map(componentClazz::cast);
    }

    public <T extends R> T getComponent(@NotNull Class<T> componentClazz) throws NoSuchElementException
    {
        return this.streamComponents(componentClazz)
            .findAny()
            .orElseThrow();
    }

    public <T extends R> T getComponent(@NotNull Class<T> componentClazz, @NotNull Comparator<T> heuristic) throws NoSuchElementException
    {
        return this.streamComponents(componentClazz)
            .max(heuristic)
            .orElseThrow();
    }

    public <T extends R> List<T> getComponents(@NotNull Class<T> componentClazz)
    {
        return this.streamComponents(componentClazz).toList();
    }

    public void removeComponent(@NotNull R component) throws IllegalArgumentException
    {
        var token = this.tokens.remove(component);

        if (token == null)
            throw new IllegalArgumentException("Component to token mapping could not be found: %d -> ???".formatted(component.getID()));

        this.components.removeMapping(token, component);

        var classes = this.implementationReceivers.remove(component);

        classes.forEach(clazz -> this.implementationProviders.removeMapping(clazz, component));

        this.onComponentRemoved(component);
    }

    @MustBeInvokedByOverriders
    protected void onComponentRemoved(R component)
    {
        try
        {
            component.destroy(this);
        }
        catch (Exception e)
        {
            throw new RuntimeException("An exception has occured whiile unmounting the component", e);
        }
    }

    public <T extends R> void removeComponents(@NotNull ComponentToken<T> componentToken)
    {
        var activeComponents = this.components.remove(componentToken);

        activeComponents.forEach(component -> {
            this.tokens.remove(component);

            var classes = this.implementationReceivers.remove(component);

            classes.forEach(clazz -> this.implementationProviders.removeMapping(clazz, component));

            this.onComponentRemoved(component);
        });
    }
}
