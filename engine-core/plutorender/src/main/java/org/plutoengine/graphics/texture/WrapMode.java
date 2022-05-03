package org.plutoengine.graphics.texture;

import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL44;
import org.plutoengine.graphics.gl.IOpenGLEnum;

import java.util.EnumSet;

public enum WrapMode implements IOpenGLEnum
{
    REPEAT(GL33.GL_REPEAT),
    CLAMP_TO_EDGE(GL33.GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL33.GL_CLAMP_TO_BORDER),
    MIRROR_CLAMP_TO_EDGE(GL44.GL_MIRROR_CLAMP_TO_EDGE),
    MIRRORED_REPEAT(GL33.GL_MIRRORED_REPEAT);

    WrapMode(int id)
    {
        this.id = id;
    }

    public static final EnumSet<WrapMode> repeatModes = EnumSet.of(WrapMode.MIRRORED_REPEAT, WrapMode.REPEAT);
    public static final EnumSet<WrapMode> clampModes = EnumSet.of(WrapMode.CLAMP_TO_EDGE, WrapMode.CLAMP_TO_BORDER, MIRROR_CLAMP_TO_EDGE);
    public static final EnumSet<WrapMode> mirrorModes = EnumSet.of(WrapMode.MIRROR_CLAMP_TO_EDGE, WrapMode.MIRRORED_REPEAT);

    private final int id;

    @Override
    public int getGLID()
    {
        return this.id;
    }
}
