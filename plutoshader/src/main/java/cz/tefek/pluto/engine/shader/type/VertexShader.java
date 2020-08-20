package cz.tefek.pluto.engine.shader.type;

import cz.tefek.pluto.engine.shader.ShaderCompiler;
import cz.tefek.pluto.io.asl.resource.ResourceAddress;

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
