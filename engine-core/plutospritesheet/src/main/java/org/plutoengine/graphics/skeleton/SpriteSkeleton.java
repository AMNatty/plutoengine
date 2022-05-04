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

package org.plutoengine.graphics.skeleton;

import org.joml.Vector2f;
import org.joml.Vector2fc;

public class SpriteSkeleton
{
    protected SpriteSkeletonLimb rootLimb;
    protected static final Vector2f DEFAULT_SCALE = new Vector2f(1);
    protected static final Vector2f DEFAULT_SCALE_FLIPPED = new Vector2f(-1, 1);

    public void render(Vector2fc position, Vector2fc scale)
    {
        this.rootLimb.render(position, scale);
    }

    public void render(Vector2fc position)
    {
        this.render(position, DEFAULT_SCALE);
    }

    public SpriteSkeletonLimb getRootLimb()
    {
        return this.rootLimb;
    }
}
