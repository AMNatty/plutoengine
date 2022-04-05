package org.plutoengine.component;

public interface IComponent
{
    /**
     * Denotes whether this component should be unique.
     * Unique components can only exist once per instance
     * in a given {@link ComponentManager}.
     *
     * @return whether this component should be unique
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    boolean isUnique();

    long getID();

    void onMount() throws Exception;

    void onUnmount() throws Exception;
}
