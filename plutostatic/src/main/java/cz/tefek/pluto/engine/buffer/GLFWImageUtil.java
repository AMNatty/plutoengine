package cz.tefek.pluto.engine.buffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import cz.tefek.pluto.tpl.TPL;

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
    public static GLFWImage.Buffer loadIconSet(String... icons)
    {
        var icon = GLFWImage.create(icons.length);

        for (int iconIndex = 0; iconIndex < icons.length; iconIndex++)
        {
            var img = TPL.loadPixels(icons[iconIndex]);
            var imgData = img.getData();
            var imgWidth = img.getWidth();
            var imgHeight = img.getHeight();

            var byteBuf = BufferUtils.createByteBuffer(imgWidth * imgHeight * 4);

            for (int i = 0; i < imgHeight * imgWidth; i++)
            {
                var data = imgData[i];

                byteBuf.put((byte) ((data & 0x00ff0000) >> 16));
                byteBuf.put((byte) ((data & 0x0000ff00) >> 8));
                byteBuf.put((byte) (data & 0x000000ff));
                byteBuf.put((byte) ((data & 0xff000000) >> 24));
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
