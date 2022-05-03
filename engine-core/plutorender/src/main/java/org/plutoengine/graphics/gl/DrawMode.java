package org.plutoengine.graphics.gl;

import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;

public enum DrawMode implements IOpenGLEnum
{
    POINTS(GL33.GL_POINTS),
    LINES(GL33.GL_LINES),
    LINE_LOOP(GL33.GL_LINE_LOOP),
    LINE_STRIP(GL33.GL_LINE_STRIP),
    TRIANGLES(GL33.GL_TRIANGLES),
    TRIANGLE_STRIP(GL33.GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(GL33.GL_TRIANGLE_FAN),
    @Deprecated
    QUADS(GL33.GL_QUADS),
    @Deprecated
    QUAD_STRIP(GL33.GL_QUAD_STRIP),
    @Deprecated
    POLYGON(GL33.GL_POLYGON),
    LINES_ADJACENCY(GL33.GL_LINES_ADJACENCY),
    LINE_STRIP_ADJACENCY(GL33.GL_LINE_STRIP_ADJACENCY),
    TRIANGLES_ADJACENCY(GL33.GL_TRIANGLES_ADJACENCY),
    TRIANGLE_STRIP_ADJACENCY(GL33.GL_TRIANGLE_STRIP_ADJACENCY),
    PATCHES(GL40.GL_PATCHES);

    private final int glID;

    DrawMode(int id)
    {
        this.glID = id;
    }

    @Override
    public int getGLID()
    {
        return this.glID;
    }
}
