package cz.tefek.pluto.engine.display;

import org.lwjgl.glfw.GLFWErrorCallback;

import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public class DisplayErrorCallback extends GLFWErrorCallback
{
    @Override
    public void invoke(int error, long description)
    {
        Logger.logf(SmartSeverity.ERROR, "GLFW Error code %d:\n", error);
        Logger.logf(getDescription(description));
    }

}
