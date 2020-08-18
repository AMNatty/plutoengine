package cz.tefek.pluto.command.resolver;

import java.util.function.Function;

public abstract class GenericResolver<R> extends AbstractResolver
{
    protected Function<String, R> func;

    public GenericResolver(Function<String, R> func)
    {
        this.func = func;
    }

    public R apply(String param)
    {
        return this.func.apply(param);
    }

    @Override
    public abstract Class<R> getOutputType();
}
