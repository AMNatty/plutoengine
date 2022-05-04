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

package org.plutoengine.util.color;

/**
 * TODO
 *
 * @since 20.2.0.0-alpha.3
 * @author 493msi
 */
public enum EnumColorFormat
{
    /**
     * 8-bit RGBA stored in a big-endian 32-bit integer
     */
    CF_INT_RGBA(4),
    /**
     * 8-bit BGRA stored in a big-endian 32-bit integer
     */
    CF_INT_BGRA(4),

    /**
     * 8-bit ARGB stored in a big-endian 32-bit integer
     */
    CF_INT_ARGB(4),
    /**
     * 8-bit ABGR stored in a big-endian 32-bit integer
     */
    CF_INT_ABGR(4),

    /**
     * 8-bit RGB stored in a big-endian 32-bit integer, the highest 8-bits are unused
     */
    CF_INT_RGB(4),
    /**
     * 8-bit RGB stored in a big-endian 32-bit integer, the highest 8-bits are unused
     */
    CF_INT_BGR(4),

    /**
     * 8-bit RGBA, one byte per color component
     */
    CF_4BYTE_RGBA(4),
    /**
     * 8-bit BGRA, one byte per color component
     */
    CF_4BYTE_BGRA(4),

    /**
     * 8-bit ARGB, one byte per color component
     */
    CF_4BYTE_ARGB(4),
    /**
     * 8-bit ABGR, one byte per color component
     */
    CF_4BYTE_ABGR(4),

    /**
     * 8-bit RGB, one byte per color component
     */
    CF_3BYTE_RGB(3),
    /**
     * 8-bit BGR, one byte per color component
     */
    CF_3BYTE_BGR(3);

    private final int size;

    EnumColorFormat(int size)
    {
        this.size = size;
    }

    /**
     * Returns the size in bytes.
     *
     * @return The size of the color format, in bytes
     */
    public int getSize()
    {
        return this.size;
    }
}
