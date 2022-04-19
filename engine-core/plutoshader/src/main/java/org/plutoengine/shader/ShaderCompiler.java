package org.plutoengine.shader;

import org.lwjgl.opengl.GL33;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.plutoengine.shader.type.EnumShaderType;
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
