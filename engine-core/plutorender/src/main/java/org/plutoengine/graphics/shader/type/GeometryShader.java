package org.plutoengine.graphics.shader.type;

import java.nio.file.Path;

import org.plutoengine.graphics.shader.ShaderCompiler;

public final class GeometryShader implements IShader
{
    private final int id;

    public GeometryShader(Path path)
    {
        this.id = ShaderCompiler.load(path, EnumShaderType.GEOMETRY);
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
