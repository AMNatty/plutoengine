package cz.tefek.pluto.command.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.tefek.pluto.command.resolver.primitive.BasicIntResolver;

class BasicIntResolverTest
{
    @Test
    void parse()
    {
        var resolver = new BasicIntResolver();

        Assertions.assertEquals(5, resolver.apply("5"));

        Assertions.assertEquals(-50, resolver.apply("-50"));

        Assertions.assertEquals(155, resolver.apply("155"));

        Assertions.assertEquals(0, resolver.apply("0"));

        Assertions.assertEquals(-0, resolver.apply("-0"));
    }

    @Test
    void exceptions()
    {
        var resolver = new BasicIntResolver();

        // No foreign characters
        Assertions.assertThrows(NumberFormatException.class, () -> resolver.apply("abc"));

        // No floats
        Assertions.assertThrows(NumberFormatException.class, () -> resolver.apply("12.5"));

        // No empty strings
        Assertions.assertThrows(NumberFormatException.class, () -> resolver.apply(""));

    }
}
