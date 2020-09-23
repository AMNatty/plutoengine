package cz.tefek.pluto.command.resolver.primitive;

import java.util.function.Function;

import cz.tefek.pluto.command.resolver.GenericResolver;

public class StringResolver extends GenericResolver<String>
{
    public StringResolver()
    {
        super(String::valueOf);
    }

    @Override
    public Class<String> getOutputType()
    {
        return String.class;
    }
}
