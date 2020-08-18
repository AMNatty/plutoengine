package cz.tefek.pluto.command.resolver.primitive;

import cz.tefek.pluto.command.resolver.EnumResolveFailure;
import cz.tefek.pluto.command.resolver.ResolveException;

public class IntFractionResolver extends IntResolver
{
    public IntFractionResolver(int base)
    {
        super(str -> parseAmount(str, base));
    }

    private static int parseAmount(String amountString, int base) throws ResolveException
    {
        if (amountString.equalsIgnoreCase("all") || amountString.equalsIgnoreCase("everything"))
        {
            return base;
        }

        if (amountString.equalsIgnoreCase("half"))
        {
            return base / 2;
        }

        if (amountString.endsWith("%"))
        {
            try
            {
                float percentage = Float.parseFloat(amountString.substring(0, amountString.length() - 1));

                if (percentage < 0 || percentage > 100)
                {
                    throw new ResolveException(EnumResolveFailure.FRAC_PERCENTAGE_OUT_OF_RANGE);
                }
                else
                {
                    return Math.round(percentage / 100.0f * base);
                }
            }
            catch (NumberFormatException e1)
            {
                throw new ResolveException(EnumResolveFailure.FRAC_INVALID_PERCENTAGE);
            }
        }

        try
        {
            int amount = Integer.parseInt(amountString);

            if (amount > base)
            {
                throw new ResolveException(EnumResolveFailure.FRAC_VALUE_HIGHER_THAN_BASE);
            }

            return amount;
        }
        catch (NumberFormatException e)
        {
            throw new ResolveException(EnumResolveFailure.INT_PARSE);
        }
    }

}
