package org.plutoengine.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardInputCallback extends GLFWKeyCallback
{
    private final Set<Integer> keyPressed = new HashSet<>();
    private final Set<Integer> keyDown = new HashSet<>();
    private final Set<Integer> keyReleased = new HashSet<>();

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        if (key < 0)
        {
            return;
        }

        if (action == GLFW_PRESS)
        {
            this.keyDown.add(key);
            this.keyPressed.add(key);
        }

        if (action == GLFW_RELEASE)
        {
            this.keyDown.remove(key);
            this.keyReleased.add(key);
        }
    }

    public void resetPressed()
    {
        this.keyPressed.clear();
        this.keyReleased.clear();
    }

    public boolean hasBeenPressed(int key)
    {
        return this.keyPressed.contains(key);
    }

    public boolean hasBeenReleased(int key)
    {
        return this.keyReleased.contains(key);
    }

    public boolean isKeyDown(int key)
    {
        return this.keyDown.contains(key);
    }
}
