/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
