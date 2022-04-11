package org.plutoengine.graphics.gui.command;

import org.plutoengine.graphics.gui.IGUIShader;
import org.plutoengine.libra.command.impl.LiCommandSwitchShader;

public class PlutoCommandSwitchShader extends LiCommandSwitchShader<IGUIShader>
{
    public PlutoCommandSwitchShader(IGUIShader shader)
    {
        super(shader);
    }
}
