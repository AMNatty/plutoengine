package cz.tefek.pluto.command.resolver.primitive;

import cz.tefek.pluto.command.resolver.EnumResolveFailure;
import cz.tefek.pluto.command.resolver.ResolveException;

public class BasicDoubleResolver extends DoubleResolver
{
    public BasicDoubleResolver()
    {
        super(str ->
        {
            try
            {
                return Double.parseDouble(str);
            }
            catch (NumberFormatException e)
            {
                throw new ResolveException(EnumResolveFailure.DOUBLE_PARSE);
            }
        });
    }

}
