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

package org.plutoengine.tpl;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Quick ABGR (8-bit per channel, 32 bits per pixel) and grayscale image loader for OpenGL textures.
 * Color component swizzling may be needed.
 *
 * @author 493msi
 *
 * @see ImageABGR
 * @see ImageY
 *
 * @since pre-alpha
 */
public class ImageLoader
{
    private static final int PLACEHOLDER_SIZE = 16;

    private static final int PLACEHOLDER_CHECKEDBOARD = 8;
    private static final int PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE = PLACEHOLDER_SIZE / PLACEHOLDER_CHECKEDBOARD;

    private static final BufferedImage placeholder;

    static
    {
        placeholder = new BufferedImage(PLACEHOLDER_SIZE, PLACEHOLDER_SIZE, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < PLACEHOLDER_SIZE * PLACEHOLDER_SIZE; i++)
        {
            int x = i % PLACEHOLDER_SIZE;
            int y = i / PLACEHOLDER_SIZE;
            boolean checker = x / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2 == y / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2;

            placeholder.setRGB(x, y, checker ? 0xFFFF0000 : 0xFF000000);
        }
    }

    private static BufferedImage loadBufferedImage(@Nullable Path path)
    {
        if (path == null)
            return placeholder;

        try (var is = Files.newInputStream(path))
        {
            return ImageIO.read(is);
        }
        catch (Exception e)
        {
            Logger.logf(SmartSeverity.ERROR, "[TPL] Image could not be loaded: %s%n", path);
            Logger.log(e);
        }

        return placeholder;
    }

    /**
     * Loads an image from a file the denoted by the input {@link Path} into a {@link ImageABGR} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param path The source {@link Path}, from which the image will be loaded
     *
     * @return The output {@link ImageABGR}, never null
     *
     * @see ImageABGR
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static ImageABGR load(@Nullable Path path)
    {
        return loadImage(loadBufferedImage(path));
    }

    /**
     * Loads an image from a file the denoted by the input {@link Path} into a {@link ImageABGR} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param path The source {@link Path}, from which the image will be loaded
     * @param flipY Whether the image should be flipped vertically (for OpenGL uses)
     *
     * @return The output {@link ImageABGR}, never null
     *
     * @see ImageABGR
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static ImageABGR loadSpecial(@Nullable Path path, boolean flipY)
    {
        return loadImageSpecial(loadBufferedImage(path), flipY);
    }

    /**
     * Writes a {@link BufferedImage} into a {@link ImageABGR} buffer.
     *
     * If the input {@link BufferedImage} is null, a placeholder will be generated.
     *
     * @param image The source {@link BufferedImage}
     * @param flipY Whether the image should be flipped vertically (for OpenGL uses)
     *
     * @return The output {@link ImageABGR}, never null
     *
     * @see ImageABGR
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static ImageABGR loadImageSpecial(@Nullable BufferedImage image, boolean flipY)
    {
        if (image == null)
        {
            Logger.log(SmartSeverity.WARNING, "[TPL] Null BufferedImage supplied, generating a placeholder.");

            return loadImageSpecial(placeholder, flipY);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        if (width > 16384 || height > 16384 || width < 1 || height < 1)
        {
            Logger.log(SmartSeverity.ERROR, "[TPL] BufferedImage size is invalid (< 1 or > 16384), generating a placeholder.");

            return loadImageSpecial(placeholder, flipY);
        }

        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D imgGraphics = copy.createGraphics();
        imgGraphics.drawImage(image,
                0, flipY ? copy.getHeight() : 0, copy.getWidth(), flipY ? 0 : copy.getHeight(),
                0, 0, image.getWidth(), image.getHeight(),
                null); // I wonder if this is pixel-perfect
        imgGraphics.dispose();

        Raster data = copy.getRaster();
        DataBuffer dataBuffer = data.getDataBuffer();
        DataBufferByte byteBuffer = (DataBufferByte) dataBuffer;
        byte[] byteData = byteBuffer.getData();
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        buffer.put(byteData);
        buffer.flip();

        return new ImageABGR(buffer, width, height);
    }


    /**
     * Writes a {@link BufferedImage} into an {@link ImageABGR} buffer.
     *
     * If the input {@link BufferedImage} is null, a placeholder will be generated.
     *
     * @param image The source {@link BufferedImage}
     *
     * @return The output {@link ImageABGR}, never null
     *
     * @see ImageABGR
     *
     * @since pre-alpha
     * */
    public static ImageABGR loadImage(@Nullable BufferedImage image)
    {
        return loadImageSpecial(image, true);
    }

    /**
     * Writes a {@link BufferedImage} into an {@link ImageY} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param image The source {@link BufferedImage}
     * @param flipY Whether the image should be flipped vertically (for OpenGL uses)
     *
     * @return The output {@link ImageY}, never null
     *
     * @see ImageY
     *
     * @since 22.1.0.0-alpha.1
     * */
    public static ImageY loadImageGrayscaleSpecial(@Nullable BufferedImage image, boolean flipY)
    {
        if (image == null)
        {
            Logger.log(SmartSeverity.WARNING, "[TPL] Null BufferedImage supplied, generating a placeholder.");

            return loadImageGrayscaleSpecial(placeholder, flipY);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        if (width > 16384 || height > 16384 || width < 1 || height < 1)
        {
            Logger.log(SmartSeverity.ERROR, "[TPL] BufferedImage size is invalid (< 1 or > 16384), generating a placeholder.");

            return loadImageGrayscaleSpecial(placeholder, flipY);
        }

        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D imgGraphics = copy.createGraphics();
        imgGraphics.drawImage(image,
            0, flipY ? copy.getHeight() : 0, copy.getWidth(), flipY ? 0 : copy.getHeight(),
            0, 0, image.getWidth(), image.getHeight(),
            null); // I wonder if this is pixel-perfect
        imgGraphics.dispose();

        Raster data = copy.getRaster();
        DataBuffer dataBuffer = data.getDataBuffer();
        DataBufferByte byteBuffer = (DataBufferByte) dataBuffer;
        byte[] byteData = byteBuffer.getData();
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height);
        buffer.put(byteData);
        buffer.flip();

        return new ImageY(buffer, width, height);
    }

    /**
     * Writes a {@link BufferedImage} into an {@link ImageY} buffer.
     *
     * If the input {@link BufferedImage} is null, a placeholder will be generated.
     *
     * @param image The source {@link BufferedImage}
     *
     * @return The output {@link ImageABGR}, never null
     *
     * @see ImageY
     *
     * @since 22.1.0.0-alpha.1
     * */
    public static ImageY loadImageGrayscale(@Nullable BufferedImage image)
    {
        return loadImageGrayscaleSpecial(image, true);
    }

}
