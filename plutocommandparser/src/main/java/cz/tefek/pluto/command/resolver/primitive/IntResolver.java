package cz.tefek.pluto.command.resolver.primitive;

import java.util.function.ToIntFunction;

import cz.tefek.pluto.command.resolver.AbstractResolver;

public class IntResolver extends AbstractResolver
{
    protected ToIntFunction<String> func;

    public IntResolver(ToIntFunction<String> func)
    {
        this.func = func;
    }

    public int apply(String param)
    {
        return this.func.applyAsInt(param);
    }

    @Override
    public final Class<?> getOutputType()
    {
        return int.class;
    }
}
