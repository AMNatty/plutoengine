package cz.tefek.pluto.command.resolver.generic;

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
