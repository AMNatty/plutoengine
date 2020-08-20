package cz.tefek.pluto.engine.gl;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBUniformBufferObject;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public class GLDebugInfo
{
    public static void printDebugInfo(GLCapabilities glCapabilities)
    {
        Logger.logf(SmartSeverity.INFO, "OpenGL20: %b\n", glCapabilities.OpenGL20);
        Logger.logf(SmartSeverity.INFO, "OpenGL21: %b\n", glCapabilities.OpenGL21);

        Logger.logf(SmartSeverity.INFO, "OpenGL30: %b\n", glCapabilities.OpenGL30);

        Logger.logf(SmartSeverity.INFO, "OpenGL33: %b\n", glCapabilities.OpenGL33);

        Logger.logf(SmartSeverity.INFO, "OpenGL40: %b\n", glCapabilities.OpenGL40);

        Logger.logf(SmartSeverity.INFO, "OpenGL45: %b\n", glCapabilities.OpenGL45);

        Logger.logf(SmartSeverity.INFO, "GL_MAX_TEXTURE_SIZE: %d\n", GL33.glGetInteger(GL33.GL_MAX_TEXTURE_SIZE));
        Logger.logf(SmartSeverity.INFO, "GL_MAX_VERTEX_ATTRIBS: %d\n", GL33.glGetInteger(GL33.GL_MAX_VERTEX_ATTRIBS));
        Logger.logf(SmartSeverity.INFO, "GL_MAX_TEXTURE_IMAGE_UNITS: %d\n", GL33.glGetInteger(GL33.GL_MAX_TEXTURE_IMAGE_UNITS));

        Logger.logf(SmartSeverity.INFO, "GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS: %d\n", GL33.glGetInteger(GL33.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));

        Logger.logf(SmartSeverity.INFO, "ARBFramebufferObject.GL_MAX_RENDERBUFFER_SIZE: %d\n", GL33.glGetInteger(ARBFramebufferObject.GL_MAX_RENDERBUFFER_SIZE));
        Logger.logf(SmartSeverity.INFO, "ARBFramebufferObject.GL_MAX_SAMPLES: %d\n", GL33.glGetInteger(ARBFramebufferObject.GL_MAX_SAMPLES));
        Logger.logf(SmartSeverity.INFO, "ARBFramebufferObject.GL_MAX_COLOR_ATTACHMENTS: %d\n", GL33.glGetInteger(ARBFramebufferObject.GL_MAX_COLOR_ATTACHMENTS));

        Logger.logf(SmartSeverity.INFO, "GL_ARB_uniform_buffer_object: %b\n", glCapabilities.GL_ARB_uniform_buffer_object);

        Logger.logf(SmartSeverity.INFO, "GL_MAX_UNIFORM_BLOCK_SIZE : %d bytes\n", GL33.glGetInteger(ARBUniformBufferObject.GL_MAX_UNIFORM_BLOCK_SIZE));

    }
}
