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

import org.jetbrains.annotations.Range;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class VirtualAddressParser
{
    private enum State
    {
        HIERARCHY_UP,
        KEY,
        PATH_SEPARATOR
    }

    private int position;

    private int depth;

    private State state;

    private final Deque<String> components;

    private StringBuilder tokenBuilder;

    private final boolean permitRelative;

    // Decrement for every root-level ~
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int rootOffset = 0;

    VirtualAddressParser(boolean permitRelative)
    {
        this.depth = 0;
        this.position = 0;
        this.state = State.PATH_SEPARATOR;
        this.components = new ArrayDeque<>(VirtualAddress.MAX_KEYS);
        this.permitRelative = permitRelative;
    }

    public void accept(int codepoint)
    {
        switch (this.state)
        {
            case PATH_SEPARATOR -> {
                if (Character.isLetter(codepoint))
                {
                    if (this.components.size() >= VirtualAddress.MAX_KEYS)
                        throw new VirtualAddressParseException("Max amount of keys (%d) exceeded!".formatted(VirtualAddress.MAX_KEYS));

                    this.state = State.KEY;
                    this.tokenBuilder = new StringBuilder(VirtualAddress.MAX_KEY_LENGTH);
                    this.tokenBuilder.appendCodePoint(codepoint);
                }
                else if (codepoint == VirtualAddress.TOKEN_HIERARCHY_UP)
                {
                    if (!this.permitRelative)
                        throw new VirtualAddressParseException("Cannot use the hierarchy-up token (%s) in a non-relative context.".formatted(VirtualAddress.TOKEN_HIERARCHY_UP));

                    this.state = State.HIERARCHY_UP;
                    this.tokenBuilder = null;

                    if (!this.components.isEmpty())
                        this.components.removeLast();
                    else
                        this.rootOffset++;
                }
                else
                {
                    throw new VirtualAddressParseException("Unexpected character at position %d: %s".formatted(this.position, Character.toString(codepoint)));
                }
            }
            case KEY -> {
                if (Character.isLetterOrDigit(codepoint) || codepoint == '_' || codepoint == '-')
                {
                    if (this.tokenBuilder.length() >= VirtualAddress.MAX_KEY_LENGTH)
                        throw new VirtualAddressParseException("Single key length (%d) exceeded!".formatted(VirtualAddress.MAX_KEY_LENGTH));

                    this.state = State.KEY;
                    this.tokenBuilder.appendCodePoint(codepoint);
                }
                else if (codepoint == VirtualAddress.TOKEN_PATH_SEPARATOR)
                {
                    if (this.depth >= VirtualAddress.MAX_KEYS)
                        throw new VirtualAddressParseException("Max amount of keys (%d) exceeded!".formatted(VirtualAddress.MAX_KEYS));

                    if (this.state != State.HIERARCHY_UP)
                        this.components.addLast(this.tokenBuilder.toString());

                    this.state = State.PATH_SEPARATOR;
                    this.depth++;
                }
                else
                {
                    throw new VirtualAddressParseException("Unexpected character at position %d: %s".formatted(this.position, Character.toString(codepoint)));
                }
            }
            case HIERARCHY_UP -> {
                if (codepoint == VirtualAddress.TOKEN_HIERARCHY_UP)
                {
                    if (!this.components.isEmpty())
                        this.components.removeLast();
                    else
                        this.rootOffset++;
                }
                else if (codepoint == VirtualAddress.TOKEN_PATH_SEPARATOR)
                {
                    if (this.depth >= VirtualAddress.MAX_KEYS)
                        throw new VirtualAddressParseException("Max amount of keys (%d) exceeded!".formatted(VirtualAddress.MAX_KEYS));

                    this.state = State.PATH_SEPARATOR;
                    this.depth++;
                }
                else
                {
                    throw new VirtualAddressParseException("Unexpected character at position %d: %s".formatted(this.position, Character.toString(codepoint)));
                }
            }
        }

        this.position++;

        if (this.rootOffset >= VirtualAddress.MAX_KEYS)
            throw new VirtualAddressParseException("Cannot move than %d levels up in the hierarchy!".formatted(VirtualAddress.MAX_KEYS));
    }

    public VirtualAddress build()
    {
        if (this.state == State.KEY && this.tokenBuilder != null)
            this.components.addLast(this.tokenBuilder.toString());

        var normalizedAddress = getNormalizedString(this.rootOffset, this.components);

        return new VirtualAddress(normalizedAddress, List.copyOf(this.components), this.permitRelative, this.rootOffset);
    }

    static String getNormalizedString(int rootOffset, Iterable<String> components)
    {
        var separator = Character.toString(VirtualAddress.TOKEN_PATH_SEPARATOR);
        var componentsJoined = String.join(separator, components);

        var sb = new StringBuilder();

        sb.append(Character.toString(VirtualAddress.TOKEN_HIERARCHY_UP).repeat(rootOffset));

        if (rootOffset > 0 && !componentsJoined.isEmpty())
            sb.append(separator);

        sb.append(componentsJoined);

        return sb.toString();
    }
}
