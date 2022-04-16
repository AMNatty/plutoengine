package org.plutoengine.component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractComponent<T extends AbstractComponent<? super T>>
{
    private static final AtomicLong ID_SOURCE = new AtomicLong();

    private final long id;

    private ComponentDependencyManager manager;

    protected AbstractComponent()
    {
        this.id = ID_SOURCE.getAndIncrement();
    }

    public long getID()
    {
        return this.id;
    }

    /**
     * Denotes whether this component should be unique.
     * Unique components can only exist once per instance
     * in a given {@link ComponentManager}.
     *
     * @return whether this component should be unique
     */
    public abstract boolean isUnique();

    void initialize(ComponentManager<? super T> manager) throws Exception
    {
        this.manager = this.new ComponentDependencyManager(manager);

        onMount(this.manager);
    }

    protected void onMount(ComponentDependencyManager manager) throws Exception
    {

    }

    void destroy(ComponentManager<? super T> manager) throws Exception
    {
        if (this.manager.dependencies != null)
        {
            this.manager.dependencies.forEach(manager::removeComponent);
            this.manager.dependencies.clear();
        }

        this.onUnmount();
    }

    protected void onUnmount() throws Exception
    {

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        AbstractComponent<?> that = (AbstractComponent<?>) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
    }

    public class ComponentDependencyManager
    {
        private final ComponentManager<? super T> manager;
        private Deque<T> dependencies;

        private ComponentDependencyManager(ComponentManager<? super T> componentManager)
        {
            this.manager = componentManager;
        }

        public <R extends T> R declareDependency(ComponentToken<R> token)
        {
            if (this.dependencies == null)
                this.dependencies = new ArrayDeque<>();

            var dependency = this.manager.addComponent(token);

            this.dependencies.push(dependency);

            return dependency;
        }
    }
}
