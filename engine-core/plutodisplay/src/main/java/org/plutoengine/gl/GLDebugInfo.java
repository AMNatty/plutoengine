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

package org.plutoengine.gl;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBUniformBufferObject;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

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
