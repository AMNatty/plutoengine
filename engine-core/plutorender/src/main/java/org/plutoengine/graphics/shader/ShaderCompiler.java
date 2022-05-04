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

package org.plutoengine.graphics.shader;

import org.lwjgl.opengl.GL33;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.plutoengine.graphics.shader.type.EnumShaderType;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

public class ShaderCompiler
{
    private static String preprocessCode(String code)
    {
        // TODO: More preprocessings options
        return code.trim();
    }

    public static int load(Path path, EnumShaderType type)
    {
        try
        {
            var sourceString = preprocessCode(Files.readString(path));
            return load(path.toString(), sourceString, type);
        }
        catch (IOException e)
        {
            Logger.logf(SmartSeverity.ERROR, "Failed to load shader: %s%n", path.toString());
            throw new UncheckedIOException(e);
        }
    }

    private static int load(String name, String code, EnumShaderType type)
    {
        int shaderID = GL33.glCreateShader(type.getGLID());
        GL33.glShaderSource(shaderID, code.trim());
        GL33.glCompileShader(shaderID);

        if (GL33.glGetShaderi(shaderID, GL33.GL_COMPILE_STATUS) == GL33.GL_FALSE)
        {
            Logger.log(SmartSeverity.ERROR, "Shader could not be compiled: " + name);
            Logger.log(GL33.glGetShaderInfoLog(shaderID));
        }

        Logger.logf(SmartSeverity.ADDED, "Shader ID %d compiled: %s\n", shaderID, name);

        return shaderID;
    }

}
