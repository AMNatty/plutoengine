package cz.tefek.pluto.engine.shader.type;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.engine.shader.ShaderCompiler;

public final class VertexShader implements IShader
{
    private int id;

    public VertexShader(ResourceAddress address)
    {
        this.id = ShaderCompiler.load(address, EnumShaderType.VERTEX);
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
