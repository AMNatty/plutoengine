package cz.tefek.pluto.engine.shader;

import org.lwjgl.opengl.GL33;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.io.asl.textio.TextIn;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;
import cz.tefek.pluto.engine.shader.type.EnumShaderType;

public class ShaderCompiler
{
    private static String preprocessCode(String code)
    {
        // TODO: More preprocessings options
        return code.trim();
    }

    public static int load(ResourceAddress address, EnumShaderType type)
    {
        var sourceString = preprocessCode(TextIn.fromAddress(address));
        return load(address.toString(), sourceString, type);
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
