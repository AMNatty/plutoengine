package cz.tefek.pluto.command.resolver.primitive;

import java.util.function.ToDoubleFunction;

import cz.tefek.pluto.command.resolver.AbstractResolver;

public class DoubleResolver extends AbstractResolver
{
    protected ToDoubleFunction<String> func;

    public DoubleResolver(ToDoubleFunction<String> func)
    {
        this.func = func;
    }

    public double apply(String param)
    {
        return this.func.applyAsDouble(param);
    }

    @Override
    public Class<?> getOutputType()
    {
        return double.class;
    }
}
