package cz.tefek.pluto.engine.graphics.texture;

import java.util.EnumSet;

import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL44;

import cz.tefek.pluto.engine.gl.IOpenGLEnum;

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

    private int id;

    @Override
    public int getGLID()
    {
        return this.id;
    }
}
