package cz.tefek.pluto.command.resolver;

public enum EnumResolveFailure
{
    INT_PARSE,
    LONG_PARSE,
    FLOAT_PARSE,
    DOUBLE_PARSE,
    FRAC_INVALID_PERCENTAGE,
    FRAC_PERCENTAGE_OUT_OF_RANGE,
    FRAC_VALUE_HIGHER_THAN_BASE,
    OTHER;
}
