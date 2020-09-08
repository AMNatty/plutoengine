package cz.tefek.pluto.tpl;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

/**
 * Quick ABGR (8-bit per channel, 32 bits per pixel) image loader for OpenGL textures.
 * Color component swizzling may be needed.
 *
 * FIXME: Refactor {@link TPL#loadBufferedImage}
 * 
 * @author 493msi
 *
 * @see TPNImage
 *
 * @since pre-alpha
 */
public class TPL
{
    private static final int PLACEHOLDER_SIZE = 16;

    private static final int PLACEHOLDER_CHECKEDBOARD = 8;
    private static final int PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE = PLACEHOLDER_SIZE / PLACEHOLDER_CHECKEDBOARD;

    private static final BufferedImage placeholder;

    static
    {
        placeholder = new BufferedImage(PLACEHOLDER_SIZE, PLACEHOLDER_SIZE, BufferedImage.TYPE_INT_ARGB);
        var data = placeholder.getData();
        var dataBuffer = (DataBufferInt) data.getDataBuffer();

        for (int i = 0; i < PLACEHOLDER_SIZE * PLACEHOLDER_SIZE; i++)
        {
            int x = i % PLACEHOLDER_SIZE;
            int y = i / PLACEHOLDER_SIZE;
            boolean checker = x / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2 == y / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2;

            dataBuffer.setElem(i, checker ? 0xFFFF0000 : 0xFF000000);
        }
    }

    // TODO: Fix this mess
    private static BufferedImage loadBufferedImage(@Nullable Object source)
    {
        var inputStream = (InputStream) null;

        try
        {
            if (source instanceof ResourceAddress)
            {
                inputStream = Files.newInputStream(((ResourceAddress) source).toNIOPath());
            }
            else if (source instanceof String)
            {
                inputStream = new FileInputStream((String) source);
            }
            else if (source instanceof File)
            {
                inputStream = new FileInputStream((File) source);
            }
            else if (source instanceof Path)
            {
                inputStream = Files.newInputStream((Path) source);
            }

            if (inputStream != null)
            {
                return ImageIO.read(inputStream);
            }
        }
        catch (Exception e)
        {
            Logger.log(SmartSeverity.ERROR, "[TPL] Image could not be loaded: " + source);
            Logger.log(e);
        }
        finally
        {
            try
            {
                if (inputStream != null)
                    inputStream.close();
            }
            catch (IOException e)
            {
                Logger.log(SmartSeverity.ERROR, "[TPL] Failed to close: " + source);
                Logger.log(e);
            }
        }

        return placeholder;
    }

    /**
     * Loads an image from a file the denoted by the input {@link ResourceAddress} into a {@link TPNImage} buffer.
     *
     * If the input {@link ResourceAddress} is null, a placeholder will be generated.
     *
     * @param address The source {@link ResourceAddress}, from which the image will be loaded
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since pre-alpha
     * */
    public static TPNImage load(@Nullable ResourceAddress address)
    {
        return loadImage(loadBufferedImage(address));
    }

    /**
     * Loads an image from the denoted filename to a {@link TPNImage} buffer.
     *
     * If the input filename is null, a placeholder will be generated.
     *
     * @deprecated Use the {@link TPL#load(ResourceAddress)} or {@link TPL#load(File)} variants.
     *
     * @param filename The source filename, from which the image will be loaded
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since pre-alpha
     * */
    @Deprecated
    public static TPNImage load(@Nullable String filename)
    {
        return loadImage(loadBufferedImage(filename));
    }

    /**
     * Loads an image from the input {@link File} into a {@link TPNImage} buffer.
     *
     * If the input {@link File} is null, a placeholder will be generated.
     *
     * @param file The source {@link File}, from which the image will be loaded
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static TPNImage load(@Nullable File file)
    {
        return loadImage(loadBufferedImage(file));
    }

    /**
     * Loads an image from a file the denoted by the input {@link Path} into a {@link TPNImage} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param path The source {@link Path}, from which the image will be loaded
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static TPNImage load(@Nullable Path path)
    {
        return loadImage(loadBufferedImage(path));
    }

    /**
     * Loads an image from a file the denoted by the input {@link ResourceAddress} into a {@link TPNImage} buffer.
     *
     * If the input {@link ResourceAddress} is null, a placeholder will be generated.
     *
     * @param address The source {@link ResourceAddress}, from which the image will be loaded
     * @param flipY Whether the image should flipped vertically (for OpenGL uses)
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static TPNImage loadSpecial(@Nullable ResourceAddress address, boolean flipY)
    {
        return loadImageSpecial(loadBufferedImage(address), flipY);
    }


    /**
     * Loads an image from the input {@link File} into a {@link TPNImage} buffer.
     *
     * If the input {@link File} is null, a placeholder will be generated.
     *
     * @param file The source {@link File}, from which the image will be loaded
     * @param flipY Whether the image should flipped vertically (for OpenGL uses)
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static TPNImage load(@Nullable File file, boolean flipY)
    {
        return loadImageSpecial(loadBufferedImage(file), flipY);
    }

    /**
     * Loads an image from a file the denoted by the input {@link Path} into a {@link TPNImage} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param path The source {@link Path}, from which the image will be loaded
     * @param flipY Whether the image should flipped vertically (for OpenGL uses)
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static TPNImage loadSpecial(@Nullable Path path, boolean flipY)
    {
        return loadImageSpecial(loadBufferedImage(path), flipY);
    }

    /**
     * Writes a {@link BufferedImage} into a {@link TPNImage} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param image The source {@link BufferedImage}
     * @param flipY Whether the image should flipped vertically (for OpenGL uses)
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since 20.2.0.0-alpha.1
     * */
    public static TPNImage loadImageSpecial(@Nullable BufferedImage image, boolean flipY)
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
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());
        buffer.put(byteData);
        buffer.flip();

        return new TPNImage(buffer, width, height);
    }


    /**
     * Writes a {@link BufferedImage} into a {@link TPNImage} buffer.
     *
     * If the input {@link Path} is null, a placeholder will be generated.
     *
     * @param image The source {@link BufferedImage}
     *
     * @return The output {@link TPNImage}, never null
     *
     * @see TPNImage
     *
     * @since pre-alpha
     * */
    public static TPNImage loadImage(@Nullable BufferedImage image)
    {
        return loadImageSpecial(image, true);
    }
}
