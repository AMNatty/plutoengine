package cz.tefek.pluto.command.resolver.primitive;

import java.util.function.ToLongFunction;

import cz.tefek.pluto.command.resolver.AbstractResolver;

public class LongResolver extends AbstractResolver
{
    protected ToLongFunction<String> func;

    public LongResolver(ToLongFunction<String> func)
    {
        this.func = func;
    }

    public long apply(String param)
    {
        return this.func.applyAsLong(param);
    }

    @Override
    public final Class<?> getOutputType()
    {
        return long.class;
    }
}
