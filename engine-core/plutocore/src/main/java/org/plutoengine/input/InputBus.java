package org.plutoengine.input;

import org.plutoengine.component.ComponentToken;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.display.Display;

public class InputBus extends PlutoLocalComponent
{
    public static ComponentToken<InputBus> fromDisplay(Display display)
    {
        return ComponentToken.create(() -> new InputBus(display));
    }

    private final Display display;

    private Keyboard keyboard;
    private Mouse mouse;

    private InputBus(Display display)
    {
        this.display = display;
    }

    @Override
    protected void onMount(ComponentDependencyManager manager)
    {
        this.keyboard = manager.declareDependency(ComponentToken.create(() -> new Keyboard(this.display.getWindowPointer())));
        this.mouse = manager.declareDependency(ComponentToken.create(() -> new Mouse(this.display.getWindowPointer())));
    }

    public void resetStates()
    {
        this.keyboard.resetStates();
        this.mouse.resetStates();
    }

    @Override
    public boolean isUnique()
    {
        return true;
    }

}
