package org.plutoengine.component;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Stream;

public class ComponentManager<R extends AbstractComponent>
{
    private final Class<R> base;

    protected final MultiValuedMap<ComponentToken<? extends R>, R> components;
    protected final Map<R, ComponentToken<? extends R>> tokens;
    protected final MultiValuedMap<Class<?>, R> implementationProviders;
    protected final MultiValuedMap<R, Class<?>> implementationReceivers;

    public ComponentManager(@Nonnull Class<R> base)
    {
        this.base = base;
        this.components = new HashSetValuedHashMap<>();
        this.tokens = new HashMap<>();
        this.implementationProviders = new HashSetValuedHashMap<>();
        this.implementationReceivers = new ArrayListValuedHashMap<>();
    }

    public <T extends R> T addComponent(@Nonnull ComponentToken<T> token)
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

        try
        {
            component.onMount();
        }
        catch (Exception e)
        {
            throw new RuntimeException("An exception has occured while mounting the component", e);
        }

        return component;
    }

    public Class<R> getComponentBase()
    {
        return this.base;
    }

    public <T extends R> Stream<T> streamComponents(@Nonnull Class<T> componentClazz)
    {
        var providers = this.implementationProviders.get(componentClazz);

        return providers.stream().map(componentClazz::cast);
    }

    public <T extends R> T getComponent(@Nonnull Class<T> componentClazz) throws NoSuchElementException
    {
        return this.streamComponents(componentClazz)
            .findAny()
            .orElseThrow();
    }

    public <T extends R> T getComponent(@Nonnull Class<T> componentClazz, @Nonnull Comparator<T> heuristic) throws NoSuchElementException
    {
        return this.streamComponents(componentClazz)
            .max(heuristic)
            .orElseThrow();
    }

    public <T extends R> List<T> getComponents(@Nonnull Class<T> componentClazz)
    {
        return this.streamComponents(componentClazz).toList();
    }

    public void removeComponent(@Nonnull R component) throws IllegalArgumentException
    {
        var token = this.tokens.remove(component);

        if (token == null)
            throw new IllegalArgumentException("Component to token mapping could not be found: %d -> ???".formatted(component.getID()));

        this.components.removeMapping(token, component);

        var classes = this.implementationReceivers.remove(component);

        classes.forEach(clazz -> this.implementationProviders.removeMapping(clazz, component));

        try
        {
            component.onUnmount();
        }
        catch (Exception e)
        {
            throw new RuntimeException("An exception has occured whiile unmounting the component", e);
        }
    }

    public <T extends R> void removeComponents(@Nonnull ComponentToken<T> componentToken)
    {
        var activeComponents = this.components.remove(componentToken);

        activeComponents.forEach(component -> {
            this.tokens.remove(component);

            var classes = this.implementationReceivers.remove(component);

            classes.forEach(clazz -> this.implementationProviders.removeMapping(clazz, component));

            try
            {
                component.onUnmount();
            }
            catch (Exception e)
            {
                throw new RuntimeException("An exception has occured whiile unmounting the component", e);
            }
        });
    }
}
