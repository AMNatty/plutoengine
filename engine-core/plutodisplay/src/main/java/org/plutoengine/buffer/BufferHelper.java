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

package org.plutoengine.buffer;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility class to handle primitive native buffers.
 * 
 * @author 493msi
 * @since 0.1
 *
 */
public final class BufferHelper
{
    /**
     * Creates an {@link IntBuffer} from the input <code>int</code> array.
     * 
     * @param data The input <code>int</code> array.
     * @return The created {@link IntBuffer}
     * 
     * @author 493msi
     * @since 0.1
     */
    public static IntBuffer intBuffer(int[] data)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Creates an {@link FloatBuffer} from the input <code>float</code> array.
     * 
     * @param data The input <code>float</code> array.
     * @return The created {@link FloatBuffer}
     * 
     * @author 493msi
     * @since 0.1
     */
    public static FloatBuffer floatBuffer(float[] data)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Creates a {@link ByteBuffer} from the input <code>byte</code> array.
     * 
     * @param data The input <code>byte</code> array.
     * @return The created {@link ByteBuffer}
     * 
     * @author 493msi
     * @since 0.1
     */
    public static ByteBuffer byteBuffer(byte[] data)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Reallocates a new, bigger {@link ByteBuffer} and copies the data from the
     * old one.
     * 
     * @param buffer The input {@link ByteBuffer}.
     * @param newCapacity The new buffer's capacity, can't be smaller than the
     * current one.
     * @return The new, bigger {@link ByteBuffer}.
     * 
     * @author 493msi
     * @since 0.1
     */
    public static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity)
    {
        if (buffer.capacity() > newCapacity)
        {
            throw new IllegalArgumentException("New capacity is smaller than the previous one.");
        }

        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Loads a file denoted by the specified {@link Path} and fills the input
     * {@link ByteBuffer} with the read bytes.
     * 
     * <p>
     * <em>Make sure the buffer can hold the entire file.</em>
     * </p>
     * 
     * @param path The file's path.
     * @param buf The input buffer to be filled with data.
     * 
     * @return The input {@link ByteBuffer}.
     * @throws IOException Upon standard I/O errors.
     * 
     * @author 493msi
     * @since 0.3
     */
    public static ByteBuffer readToByteBuffer(Path path, ByteBuffer buf) throws IOException
    {
        try (var fc = FileChannel.open(path))
        {
            fc.read(buf);
            buf.flip();
            return buf;
        }
    }

    /**
     * Loads a file denoted by the specified {@link Path} and returns
     * a {@link ByteBuffer} containing the read bytes.
     * 
     * @param path The file's path.
     * @return A {@link ByteBuffer} containing the file's contents.
     * @throws IOException Upon standard I/O errors.
     * 
     * @author 493msi
     * @since 0.3
     */
    public static ByteBuffer readToByteBuffer(Path path) throws IOException
    {
        return byteBuffer(Files.readAllBytes(path));
    }
}
