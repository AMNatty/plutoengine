package cz.tefek.pluto.engine.shader.type;

import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.engine.shader.ShaderCompiler;

public final class FragmentShader implements IShader
{
    private int id;

    public FragmentShader(ResourceAddress address)
    {
        this.id = ShaderCompiler.load(address, EnumShaderType.FRAGMENT);
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
