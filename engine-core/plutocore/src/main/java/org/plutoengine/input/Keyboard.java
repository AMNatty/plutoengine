package org.plutoengine.input;

import org.lwjgl.glfw.GLFW;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.input.callback.KeyboardCharInput;
import org.plutoengine.input.callback.KeyboardInputCallback;

public class Keyboard extends PlutoLocalComponent
{
    private final KeyboardInputCallback keyboard = new KeyboardInputCallback();
    private final KeyboardCharInput charInput = new KeyboardCharInput();

    private final long windowPointer;

    Keyboard(long windowPointer)
    {
        this.windowPointer = windowPointer;
    }

    public KeyboardInputCallback keyboard()
    {
        return this.keyboard;
    }

    public KeyboardCharInput charInput()
    {
        return this.charInput;
    }

    void resetStates()
    {
        this.keyboard.resetPressed();
        this.charInput.reset();
    }

    @Override
    protected void onMount(ComponentDependencyManager manager)
    {
        GLFW.glfwSetKeyCallback(this.windowPointer, this.keyboard);
        GLFW.glfwSetCharCallback(this.windowPointer, this.charInput);
    }

    @Override
    protected void onUnmount() throws Exception
    {
        GLFW.glfwSetKeyCallback(this.windowPointer, null);
        GLFW.glfwSetCharCallback(this.windowPointer, null);

        this.keyboard.free();
        this.charInput.free();
    }

    public boolean pressed(int key)
    {
        return this.keyboard.hasBeenPressed(key);
    }

    public boolean released(int key)
    {
        return this.keyboard.hasBeenReleased(key);
    }

    public boolean isKeyDown(int key)
    {
        return this.keyboard.isKeyDown(key);
    }

    public String getTypedText()
    {
        return this.charInput.getTypedText();
    }

    @Override
    public boolean isUnique()
    {
        return false;
    }
}
