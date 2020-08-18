package cz.tefek.tpl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;

/**
 * Quick ABGR (8-bit per channel, 32 bits per pixel) texture loader for OpenGL
 * use. Color component swizzling may be needed.
 * 
 * @author 493msi
 */
public class TPL
{
    private static final int PLACEHOLDER_SIZE = 16;
    private static final int PLACEHOLDER_CHECKEDBOARD = 16;
    private static final int PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE = PLACEHOLDER_SIZE / PLACEHOLDER_CHECKEDBOARD;

    public static TPNImage load(ResourceAddress file)
    {
        return file == null ? loadImage(null) : load(file.toPath());
    }

    public static TPNImage load(String file)
    {
        if (file == null)
        {
            return loadImage(null);
        }

        try
        {
            return loadImage(ImageIO.read(new File(file)));
        }
        catch (Exception e)
        {
            Logger.log(SmartSeverity.ERROR, "[TPL] Image could not be loaded: " + file);
            Logger.logException(e);

            return loadImage(null);
        }
    }

    public static TPNImage loadImage(BufferedImage image)
    {
        boolean remake = false;
        int width = 0;
        int height = 0;

        if (image == null)
        {
            Logger.log(SmartSeverity.WARNING, "[TPL] Null BufferedImage supplied, generating a placeholder.");

            remake = true;
        }
        else
        {
            width = image.getWidth();
            height = image.getHeight();

            if (width > 16384 || height > 16384 || width < 1 || height < 1)
            {
                Logger.log(SmartSeverity.ERROR, "[TPL] BufferedImage size is invalid (< 1 or > 16384), generating a placeholder.");

                remake = true;
            }
        }

        if (remake)
        {
            width = PLACEHOLDER_SIZE;
            height = PLACEHOLDER_SIZE;

            Logger.log(SmartSeverity.INFO, "[TPL] Generating a substitute image...");

            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
            buffer.order(ByteOrder.nativeOrder());

            for (int i = 0; i < width * height; i++)
            {
                int x = i % width;
                int y = i / width;
                boolean checker = x / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2 == y / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2;

                buffer.put((byte) 0xff); // A
                buffer.put((byte) 0x00); // B
                buffer.put((byte) 0x00); // G
                buffer.put((byte) (checker ? 0xff : 0x00)); // R
            }

            buffer.flip();
            return new TPNImage(buffer, width, height);
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());

        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D imgGraphics = copy.createGraphics();
        imgGraphics.drawImage(image, 0, copy.getHeight(), copy.getWidth(), 0, 0, 0, image.getWidth(), image.getHeight(), null); // I wonder if this is pixel-perfect
        imgGraphics.dispose();

        Raster data = copy.getData();
        DataBuffer dataBuffer = data.getDataBuffer();
        DataBufferByte byteBuffer = (DataBufferByte) dataBuffer;
        byte[] byteData = byteBuffer.getData();
        buffer.put(byteData);
        buffer.flip();

        return new TPNImage(buffer, width, height);
    }

    public static TPJImage loadPixels(String file)
    {
        TPJImage tImg = null;
        BufferedImage image = null;

        boolean remake = false;

        int width = 0;
        int height = 0;

        try
        {
            image = ImageIO.read(new File(file));
            width = image.getWidth();
            height = image.getHeight();
        }
        catch (Exception e)
        {
            Logger.log(SmartSeverity.ERROR, "[TPL] Image could not be loaded: " + file);
            Logger.logException(e);

            remake = true;
        }

        if (width > 16384 || height > 16384 || width < 1 || height < 1)
        {
            Logger.log(SmartSeverity.ERROR, "[TPL] Image size is invalid (< 1 or > 16384): " + file);
            Logger.log(SmartSeverity.ERROR, "[TPL] A replacement will be generated.");

            remake = true;
        }

        if (remake)
        {
            width = PLACEHOLDER_SIZE;
            height = PLACEHOLDER_SIZE;

            tImg = new TPJImage(new int[width * height], width, height);

            Logger.log(SmartSeverity.INFO, "[TPL] Generating a substitute image...");

            for (int i = 0; i < width * height; i++)
            {
                int x = i % width;
                int y = i / width;
                boolean checker = x / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2 == y / PLACEHOLDER_CHECKEDBOARD_SQUARE_SIZE % 2;

                tImg.data[i] = checker ? 0xffff0000 : 0xff000000;
            }

            return tImg;
        }

        tImg = new TPJImage(new int[width * height], width, height);

        for (int i = 0; i < width * height; i++)
        {
            int pixel = image.getRGB(i % width, i / width);

            tImg.data[i] = pixel;
        }

        return tImg;
    }
}
