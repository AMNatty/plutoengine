package cz.tefek.pluto.engine.shader.type;

import cz.tefek.pluto.engine.shader.ShaderCompiler;
import cz.tefek.pluto.io.asl.resource.ResourceAddress;

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
