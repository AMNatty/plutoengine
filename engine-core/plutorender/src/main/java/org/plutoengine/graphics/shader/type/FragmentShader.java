package org.plutoengine.graphics.shader.type;

import org.plutoengine.graphics.shader.ShaderCompiler;

import java.nio.file.Path;

public final class FragmentShader implements IShader
{
    private final int id;

    public FragmentShader(Path path)
    {
        this.id = ShaderCompiler.load(path, EnumShaderType.FRAGMENT);
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
