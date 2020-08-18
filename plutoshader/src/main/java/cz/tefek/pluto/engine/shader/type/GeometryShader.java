package cz.tefek.pluto.engine.shader.type;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.engine.shader.ShaderCompiler;

public final class GeometryShader implements IShader
{
    private int id;

    public GeometryShader(ResourceAddress address)
    {
        this.id = ShaderCompiler.load(address, EnumShaderType.GEOMETRY);
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
