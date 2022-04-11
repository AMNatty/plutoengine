package org.plutoengine.graphics.gui.command;

import org.plutoengine.graphics.texture.Texture;
import org.plutoengine.libra.command.impl.LiCommandSwitchTexture;

public class PlutoCommandSwitchTexture extends LiCommandSwitchTexture<Texture>
{
    public PlutoCommandSwitchTexture(Texture texture)
    {
        super(texture);
    }
}
