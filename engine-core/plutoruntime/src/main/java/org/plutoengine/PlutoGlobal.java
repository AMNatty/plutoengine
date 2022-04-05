package org.plutoengine;

import org.plutoengine.component.ComponentManager;
import org.plutoengine.component.PlutoGlobalComponent;

public class PlutoGlobal
{
    public static final ComponentManager<PlutoGlobalComponent> COMPONENTS = new ComponentManager<>(PlutoGlobalComponent.class);
}
