package cz.tefek.pluto.engine.input;

import org.lwjgl.glfw.GLFW;

import cz.tefek.pluto.engine.display.Display;

public class InputBus
{
    private static ThreadLocal<KeyboardInputCallback> keyboard = new ThreadLocal<>();
    private static ThreadLocal<MouseButtonCallback> mouseButton = new ThreadLocal<>();
    private static ThreadLocal<CursorPositionCallback> cursorPosition = new ThreadLocal<>();
    private static ThreadLocal<ScrollInputCallback> scroll = new ThreadLocal<>();

    public static void init(Display display)
    {
        keyboard.set(new KeyboardInputCallback());
        GLFW.glfwSetKeyCallback(display.getWindowPointer(), keyboard.get());

        mouseButton.set(new MouseButtonCallback());
        GLFW.glfwSetMouseButtonCallback(display.getWindowPointer(), mouseButton.get());

        cursorPosition.set(new CursorPositionCallback());
        GLFW.glfwSetCursorPosCallback(display.getWindowPointer(), cursorPosition.get());

        scroll.set(new ScrollInputCallback());
        GLFW.glfwSetScrollCallback(display.getWindowPointer(), scroll.get());

    }

    public static void destroy()
    {
        scroll.get().free();
        scroll.remove();
        mouseButton.get().free();
        mouseButton.remove();
        keyboard.get().free();
        keyboard.remove();
        cursorPosition.get().free();
        cursorPosition.remove();
    }

    public static KeyboardInputCallback keyboard()
    {
        return keyboard.get();
    }

    public static MouseButtonCallback mouseButtons()
    {
        return mouseButton.get();
    }

    public static ScrollInputCallback scroll()
    {
        return scroll.get();
    }

    public static CursorPositionCallback cursorPosition()
    {
        return cursorPosition.get();
    }

    public static void resetStates()
    {
        keyboard.get().resetPressed();
        mouseButton.get().reset();
        scroll.get().reset();
        cursorPosition.get().reset();
    }

    public static class Mouse
    {
        public static boolean clicked(int button)
        {
            var mb = mouseButton.get();
            return mb.buttonClicked[button];
        }

        public static boolean released(int button)
        {
            var mb = mouseButton.get();
            return mb.buttonReleased[button];
        }

        public static boolean isButtonDown(int button)
        {
            var mb = mouseButton.get();
            return mb.buttonDown[button];
        }

        public static double getX()
        {
            return cursorPosition.get().getX();
        }

        public static double getY()
        {
            return cursorPosition.get().getY();
        }

        public static double getDX()
        {
            return cursorPosition.get().getDeltaX();
        }

        public static double getDY()
        {
            return cursorPosition.get().getDeltaY();
        }

        public static double getScrollX()
        {
            return scroll.get().getXScroll();
        }

        public static double getScrollY()
        {
            return scroll.get().getYScroll();
        }
    }

    public static class Keyboard
    {
        public static boolean pressed(int key)
        {
            return keyboard.get().hasBeenPressed(key);
        }

        public static boolean released(int key)
        {
            return keyboard.get().hasBeenReleased(key);
        }

        public static boolean isKeyDown(int key)
        {
            return keyboard.get().isKeyDown(key);
        }
    }
}
