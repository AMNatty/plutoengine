package cz.tefek.pluto.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardInputCallback extends GLFWKeyCallback
{
    private Set<Integer> keyPressed = new HashSet<>();
    private Set<Integer> keyDown = new HashSet<>();
    private Set<Integer> keyReleased = new HashSet<>();

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
