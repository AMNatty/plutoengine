package org.plutoengine.input;

import org.lwjgl.glfw.GLFW;

import org.plutoengine.PlutoLocal;
import org.plutoengine.address.ThreadSensitive;
import org.plutoengine.component.ComponentToken;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.display.Display;

@ThreadSensitive(localContexts = true)
public class InputBus extends PlutoLocalComponent
{
    public static final ComponentToken<InputBus> TOKEN = ComponentToken.create(InputBus::new);

    private final KeyboardInputCallback keyboard = new KeyboardInputCallback();
    private final MouseButtonCallback mouseButton = new MouseButtonCallback();
    private final CursorPositionCallback cursorPosition = new CursorPositionCallback();
    private final ScrollInputCallback scroll = new ScrollInputCallback();
    private final KeyboardCharInput charInput = new KeyboardCharInput();

    private long windowPointer;

    private InputBus()
    {

    }

    private static InputBus instance()
    {
        return PlutoLocal.components().getComponent(InputBus.class);
    }

    @Override
    public void onMount()
    {
        var display = PlutoLocal.components().getComponent(Display.class);
        this.windowPointer = display.getWindowPointer();

        GLFW.glfwSetKeyCallback(this.windowPointer, this.keyboard);
        GLFW.glfwSetMouseButtonCallback(this.windowPointer, this.mouseButton);
        GLFW.glfwSetCursorPosCallback(this.windowPointer, this.cursorPosition);
        GLFW.glfwSetScrollCallback(this.windowPointer, this.scroll);
        GLFW.glfwSetCharCallback(this.windowPointer, this.charInput);
    }


    @Override
    public void onUnmount()
    {
        GLFW.glfwSetKeyCallback(this.windowPointer, null);
        GLFW.glfwSetMouseButtonCallback(this.windowPointer, null);
        GLFW.glfwSetCursorPosCallback(this.windowPointer, null);
        GLFW.glfwSetScrollCallback(this.windowPointer, null);
        GLFW.glfwSetCharCallback(this.windowPointer, null);

        this.scroll.free();
        this.mouseButton.free();
        this.keyboard.free();
        this.cursorPosition.free();
        this.charInput.free();
    }

    public static KeyboardInputCallback keyboard()
    {
        return instance().keyboard;
    }

    public static MouseButtonCallback mouseButtons()
    {
        return instance().mouseButton;
    }

    public static ScrollInputCallback scroll()
    {
        return instance().scroll;
    }

    public static CursorPositionCallback cursorPosition()
    {
        return instance().cursorPosition;
    }

    public static KeyboardCharInput charInput()
    {
        return instance().charInput;
    }

    public static void resetStates()
    {
        var instance = instance();

        instance.keyboard.resetPressed();
        instance.mouseButton.reset();
        instance.scroll.reset();
        instance.cursorPosition.reset();
        instance.charInput.reset();
    }

    @Override
    public boolean isUnique()
    {
        return true;
    }

    @ThreadSensitive(localContexts = true)
    public static class Mouse
    {
        public static boolean clicked(int button)
        {
            return instance().mouseButton.buttonClicked[button];
        }

        public static boolean released(int button)
        {
            return instance().mouseButton.buttonReleased[button];
        }

        public static boolean isButtonDown(int button)
        {
            return instance().mouseButton.buttonDown[button];
        }

        public static double getX()
        {
            return instance().cursorPosition.getX();
        }

        public static double getY()
        {
            return instance().cursorPosition.getY();
        }

        public static boolean isInside(int x1, int y1, int x2, int y2)
        {
            return instance().cursorPosition.isInside(x1, y1, x2, y2);
        }

        public static double getDX()
        {
            return instance().cursorPosition.getDeltaX();
        }

        public static double getDY()
        {
            return instance().cursorPosition.getDeltaY();
        }

        public static double getScrollX()
        {
            return instance().scroll.getXScroll();
        }

        public static double getScrollY()
        {
            return instance().scroll.getYScroll();
        }
    }

    @ThreadSensitive(localContexts = true)
    public static class Keyboard
    {
        public static boolean pressed(int key)
        {
            return instance().keyboard.hasBeenPressed(key);
        }

        public static boolean released(int key)
        {
            return instance().keyboard.hasBeenReleased(key);
        }

        public static boolean isKeyDown(int key)
        {
            return instance().keyboard.isKeyDown(key);
        }

        public static String getTypedText()
        {
            return instance().charInput.getTypedText();
        }
    }
}
