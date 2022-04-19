package org.plutoengine.shader.type;

import java.nio.file.Path;

import org.plutoengine.shader.ShaderCompiler;

public final class VertexShader implements IShader
{
    private final int id;

    public VertexShader(Path path)
    {
        this.id = ShaderCompiler.load(path, EnumShaderType.VERTEX);
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
