package cz.tefek.pluto.command.resolver.primitive;

import cz.tefek.pluto.command.resolver.EnumResolveFailure;
import cz.tefek.pluto.command.resolver.ResolveException;

public class BasicIntResolver extends IntResolver
{
    public BasicIntResolver()
    {
        super(str ->
        {
            try
            {
                return Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                throw new ResolveException(EnumResolveFailure.INT_PARSE);
            }
        });
    }
}
