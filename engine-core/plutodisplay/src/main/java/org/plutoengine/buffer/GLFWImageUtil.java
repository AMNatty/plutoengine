package org.plutoengine.buffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.plutoengine.tpl.ImageLoader;

import java.nio.file.Path;

/**
 * A utility class to load image files for use in GLFW.
 * 
 * @author 493msi
 * @since 0.2
 *
 */
public class GLFWImageUtil
{
    /**
     * Loads a set of image files for use in GLFW. Loads a placeholder and prints an
     * error message on missing files.
     * 
     * @param icons An array of path strings
     * @return The resulting buffer struct containing loaded icons or placeholders.
     * 
     * @author 493msi
     * @since 0.2
     */
    public static GLFWImage.Buffer loadIconSet(Path... icons)
    {
        var icon = GLFWImage.create(icons.length);

        for (var iconPath : icons)
        {
            var img = ImageLoader.loadSpecial(iconPath, false);
            var imgData = img.getData();
            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            int pixelCount = imgWidth * imgHeight;
            int bytesPerPixel = 4;
            var byteBuf = BufferUtils.createByteBuffer(pixelCount * bytesPerPixel);

            byte[] px = new byte[bytesPerPixel];

            for (int i = 0; i < pixelCount; i++)
            {
                px[3] = imgData.get(); // A
                px[2] = imgData.get(); // B
                px[1] = imgData.get(); // G
                px[0] = imgData.get(); // R

                byteBuf.put(px);
            }

            byteBuf.flip();

            var glfwImg = GLFWImage.create();
            glfwImg.set(imgWidth, imgHeight, byteBuf);

            icon.put(glfwImg);
        }

        icon.flip();
        return icon;
    }
}
