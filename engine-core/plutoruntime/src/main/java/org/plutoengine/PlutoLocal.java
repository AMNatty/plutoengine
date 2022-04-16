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
