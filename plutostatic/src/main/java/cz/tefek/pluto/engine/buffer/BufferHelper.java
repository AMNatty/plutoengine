package cz.tefek.pluto.engine.buffer;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
    public static IntBuffer flippedIntBuffer(int[] data)
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
    public static FloatBuffer flippedFloatBuffer(float[] data)
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
    public static ByteBuffer flippedByteBuffer(byte[] data)
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
     * Loads a file denoted by the specified path and returns a
     * {@link ByteBuffer} containing the read bytes.
     * 
     * @param path The file's path.
     * @return A {@link ByteBuffer} containing the file's contents.
     * @throws IOException Upon standard I/O errors.
     * 
     * @author 493msi
     * @since 0.1
     */
    public static ByteBuffer readToFlippedByteBuffer(String path) throws IOException
    {
        try (var fis = new FileInputStream(path))
        {
            var ba = IOUtils.toByteArray(fis);
            return flippedByteBuffer(ba);
        }
    }
}
