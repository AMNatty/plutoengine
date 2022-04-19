package org.plutoengine.address;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class VirtualAddressTest
{
    public static void main(String[] args)
    {
        System.out.println();

        var basic = VirtualAddress.parse("cz.tefek.pluto");
        System.out.println(basic);

        // ~.~ - ~.~.~.a => ~.a
        // a.b.c.d - ~.a.b.c.d => ~.~.~.~.~.a.b.c.d
        // a.b.c.d - d => ~.~.~.~.d
        // a.b.x.d - a.b.c.d => ~.~.c.d
        // a.b.c - d => ~.~.~.d
        // a.b.c - a.b => ~
        // a.b.c - a.e.g => ~.~.e.g

        var testCases = List.of(
            Pair.of("~.~", "~.~.~.a"),
            Pair.of("a.b.c.d", "~.a.b.c.d"),
            Pair.of("a.b.c.d", "d"),
            Pair.of("a.b.x.d", "a.b.c.d"),
            Pair.of("a.b.c", "d"),
            Pair.of("a.b.c", "a.b"),
            Pair.of("a.b.c", "a.e.g"),
            Pair.of("a.b.c", "a.b.c"),
            Pair.of("a.b.c", "a.b.c.d")
        );

        testCases.forEach(pair -> {
            var a = VirtualAddress.parse(pair.getLeft(), true);
            var b = VirtualAddress.parse(pair.getRight(), true);
            System.out.printf("\"%s\" - \"%s\" => \"%s\"%n", a, b, a.relativize(b));
        });

        System.out.println();
    }
}
