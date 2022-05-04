/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
