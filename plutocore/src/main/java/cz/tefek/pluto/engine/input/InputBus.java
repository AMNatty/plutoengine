package cz.tefek.pluto.engine.input;

import org.lwjgl.glfw.GLFW;

import cz.tefek.pluto.annotation.ThreadSensitive;
import cz.tefek.pluto.engine.display.Display;

@ThreadSensitive(localContexts = true)
public class InputBus
{
    private static final ThreadLocal<InputBus> INSTANCE = new ThreadLocal<>();

    private final KeyboardInputCallback keyboard = new KeyboardInputCallback();
    private final MouseButtonCallback mouseButton = new MouseButtonCallback();
    private final CursorPositionCallback cursorPosition = new CursorPositionCallback();
    private final ScrollInputCallback scroll = new ScrollInputCallback();
    private final KeyboardCharInput charInput = new KeyboardCharInput();

    private InputBus()
    {
    }

    public static void init(Display display)
    {
        var instance = new InputBus();

        GLFW.glfwSetKeyCallback(display.getWindowPointer(), instance.keyboard);
        GLFW.glfwSetMouseButtonCallback(display.getWindowPointer(), instance.mouseButton);
        GLFW.glfwSetCursorPosCallback(display.getWindowPointer(), instance.cursorPosition);
        GLFW.glfwSetScrollCallback(display.getWindowPointer(), instance.scroll);
        GLFW.glfwSetCharCallback(display.getWindowPointer(), instance.charInput);

        INSTANCE.set(instance);
    }

    public static void destroy()
    {
        var instance = INSTANCE.get();

        instance.scroll.free();
        instance.mouseButton.free();
        instance.keyboard.free();
        instance.cursorPosition.free();
        instance.charInput.free();
    }

    public static KeyboardInputCallback keyboard()
    {
        return INSTANCE.get().keyboard;
    }

    public static MouseButtonCallback mouseButtons()
    {
        return INSTANCE.get().mouseButton;
    }

    public static ScrollInputCallback scroll()
    {
        return INSTANCE.get().scroll;
    }

    public static CursorPositionCallback cursorPosition()
    {
        return INSTANCE.get().cursorPosition;
    }

    public static KeyboardCharInput charInput()
    {
        return INSTANCE.get().charInput;
    }

    public static void resetStates()
    {
        var instance = INSTANCE.get();

        instance.keyboard.resetPressed();
        instance.mouseButton.reset();
        instance.scroll.reset();
        instance.cursorPosition.reset();
        instance.charInput.reset();
    }

    @ThreadSensitive(localContexts = true)
    public static class Mouse
    {
        public static boolean clicked(int button)
        {
            return INSTANCE.get().mouseButton.buttonClicked[button];
        }

        public static boolean released(int button)
        {
            return INSTANCE.get().mouseButton.buttonReleased[button];
        }

        public static boolean isButtonDown(int button)
        {
            return INSTANCE.get().mouseButton.buttonDown[button];
        }

        public static double getX()
        {
            return INSTANCE.get().cursorPosition.getX();
        }

        public static double getY()
        {
            return INSTANCE.get().cursorPosition.getY();
        }

        public static boolean isInside(int x1, int y1, int x2, int y2)
        {
            return INSTANCE.get().cursorPosition.isInside(x1, y1, x2, y2);
        }

        public static double getDX()
        {
            return INSTANCE.get().cursorPosition.getDeltaX();
        }

        public static double getDY()
        {
            return INSTANCE.get().cursorPosition.getDeltaY();
        }

        public static double getScrollX()
        {
            return INSTANCE.get().scroll.getXScroll();
        }

        public static double getScrollY()
        {
            return INSTANCE.get().scroll.getYScroll();
        }
    }

    @ThreadSensitive(localContexts = true)
    public static class Keyboard
    {
        public static boolean pressed(int key)
        {
            return INSTANCE.get().keyboard.hasBeenPressed(key);
        }

        public static boolean released(int key)
        {
            return INSTANCE.get().keyboard.hasBeenReleased(key);
        }

        public static boolean isKeyDown(int key)
        {
            return INSTANCE.get().keyboard.isKeyDown(key);
        }

        public static String getTypedText()
        {
            return INSTANCE.get().charInput.getTypedText();
        }
    }
}
