package cz.tefek.pluto.engine.input;

import org.lwjgl.glfw.GLFWCharCallback;

public class KeyboardCharInput extends GLFWCharCallback
{
    private StringBuilder typedText = new StringBuilder();

    @Override
    public void invoke(long window, int codepoint)
    {
        this.typedText.appendCodePoint(codepoint);
    }

    public String getTypedText()
    {
        return this.typedText.toString();
    }

    public void reset()
    {
        this.typedText.setLength(0);
    }

}
