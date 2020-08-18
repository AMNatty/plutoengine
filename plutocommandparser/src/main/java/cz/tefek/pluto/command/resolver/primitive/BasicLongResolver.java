package cz.tefek.pluto.command.resolver.primitive;

import cz.tefek.pluto.command.resolver.EnumResolveFailure;
import cz.tefek.pluto.command.resolver.ResolveException;

public class BasicLongResolver extends LongResolver
{
    public BasicLongResolver()
    {
        super(str ->
        {
            try
            {
                return Long.parseLong(str);
            }
            catch (NumberFormatException e)
            {
                throw new ResolveException(EnumResolveFailure.LONG_PARSE);
            }
        });
    }

}
