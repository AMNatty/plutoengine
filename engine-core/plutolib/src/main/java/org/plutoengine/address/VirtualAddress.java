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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@JsonDeserialize(using = VirtualAddress.Deserializer.class, keyUsing = VirtualAddress.KeyAddrDeserializer.class)
@JsonSerialize(using = VirtualAddress.Serializer.class, keyUsing = VirtualAddress.Serializer.class)
public final class VirtualAddress implements Comparable<VirtualAddress>
{
    public static final int MAX_LENGTH = 128;
    public static final int MAX_KEYS = 32;
    public static final int MAX_KEY_LENGTH = 32;

    public static final int TOKEN_PATH_SEPARATOR = '.';
    public static final int TOKEN_HIERARCHY_UP = '~';

    private static final VirtualAddress ROOT_ADDRESS = new VirtualAddress("", List.of(), false, 0);

    private final String fullPath;

    private final List<String> components;

    private final boolean relative;

    @Range(from = 0, to = Integer.MAX_VALUE)
    private final int rootOffset;

    VirtualAddress(String fullPath, List<String> components, boolean relative, int rootOffset)
    {
        this.fullPath = fullPath;
        this.components = Collections.unmodifiableList(components);
        this.relative = relative;
        this.rootOffset = rootOffset;
    }

    public static VirtualAddress ofRoot()
    {
        return ROOT_ADDRESS;
    }

    public List<String> getComponents()
    {
        return this.components;
    }

    public boolean isRelative()
    {
        return this.relative;
    }

    public boolean isEmpty()
    {
        return this.fullPath.isEmpty();
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    public int getRootOffset()
    {
        return this.rootOffset;
    }

    @Override
    public String toString()
    {
        return this.fullPath;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || this.getClass() != o.getClass())
            return false;

        VirtualAddress that = (VirtualAddress) o;
        return this.fullPath.equals(that.fullPath);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.fullPath);
    }

    public static VirtualAddress parse(String inputStr)
    {
        return parse(inputStr, false);
    }

    public static VirtualAddress parse(String inputStr, boolean permitRelative)
    {
        var parser = new VirtualAddressParser(permitRelative);
        inputStr.codePoints()
            .sequential()
            .forEachOrdered(parser::accept);
        return parser.build();
    }

    public static VirtualAddressParser createParser(boolean permitRelative)
    {
        return new VirtualAddressParser(permitRelative);
    }

    @Override
    public int compareTo(VirtualAddress o)
    {
        if (this.rootOffset != o.rootOffset)
            return this.rootOffset - o.rootOffset;

        return this.fullPath.compareToIgnoreCase(o.fullPath);
    }

    public int getNameCount()
    {
        return this.components.size() + this.rootOffset;
    }

    public @NotNull VirtualAddress getName(int index)
    {
        if (index < this.rootOffset)
            return new VirtualAddress("", List.of(), true, 1);

        var component = this.components.get(index - this.rootOffset);

        return new VirtualAddress(component, List.of(component), true, 0);
    }

    public @NotNull VirtualAddress getParent()
    {
        if (this.components.isEmpty())
        {
            if (!this.relative)
            {
                throw new IllegalStateException("Cannot get a parent of a root absolute address!");
            }
            else
            {
                var offset = this.rootOffset - 1;
                var components = List.<String>of();
                return new VirtualAddress(VirtualAddressParser.getNormalizedString(offset, components), components, true, offset);
            }
        }

        var componentsNew = this.components.subList(0, this.components.size() - 1);
        return new VirtualAddress(VirtualAddressParser.getNormalizedString(this.rootOffset, componentsNew), componentsNew, this.relative, this.rootOffset);
    }

    public @NotNull VirtualAddress relativize(@NotNull VirtualAddress other)
    {
        if (this.relative != other.relative)
            throw new IllegalArgumentException("Cannot relativize an address when only one of the inputs is absolute!");

        if (this.relative && this.rootOffset > other.rootOffset)
            throw new IllegalArgumentException("Cannot relativize against a relative address with a root offset higher than the target one!");

        if (this.isEmpty())
            return other;

        int newOffset = other.rootOffset - this.rootOffset;

        var thIt = this.components.iterator();
        var oIt = other.components.iterator();

        var newPath = new ArrayList<String>();

        if (newOffset == 0)
        {
            while (thIt.hasNext() && oIt.hasNext())
            {
                var thComp = thIt.next();
                var oComp = oIt.next();

                if (!thComp.equals(oComp))
                {
                    newOffset++;
                    newPath.add(oComp);
                    break;
                }
            }
        }

        while (thIt.hasNext())
        {
            newOffset++;
            thIt.next();
        }

        while (oIt.hasNext())
            newPath.add(oIt.next());

        return new VirtualAddress(VirtualAddressParser.getNormalizedString(newOffset, newPath), newPath, true, newOffset);
    }

    public @NotNull VirtualAddress resolve(@NotNull VirtualAddress other)
    {
        if (!other.relative)
            return other;

        int removedComponentsSigned = other.rootOffset - this.components.size();
        int newOffset = this.rootOffset + Math.max(removedComponentsSigned, 0);

        if (!this.relative && newOffset > 0)
            throw new IllegalArgumentException("Cannot resolve a relative address against an absolute address that would make it higher than the root!");

        int compListEnd = Math.max(-removedComponentsSigned, 0);
        var componentsNew = Stream.concat(this.components.stream().limit(compListEnd), other.components.stream()).toList();

        return new VirtualAddress(VirtualAddressParser.getNormalizedString(newOffset, componentsNew), componentsNew, this.relative, newOffset);
    }

    public @NotNull VirtualAddress subAddress(int startIndex, int endIndex)
    {
        var componentCount = this.components.size();
        var maxComponents = this.rootOffset + componentCount;

        if (startIndex == 0 && endIndex == 0)
        {
            var components = List.<String>of();
            return new VirtualAddress(VirtualAddressParser.getNormalizedString(0, components), components, this.relative, 0);
        }

        if (startIndex < 0 || endIndex > maxComponents || endIndex > startIndex)
            throw new IndexOutOfBoundsException();

        var newComponents = this.components.subList(startIndex - this.rootOffset, endIndex - this.rootOffset);
        var newOffset = Math.max(0, this.rootOffset - startIndex);

        return new VirtualAddress(VirtualAddressParser.getNormalizedString(newOffset, newComponents), newComponents, this.relative, newOffset);
    }

    public boolean startsWith(@NotNull VirtualAddress other)
    {
        return this.fullPath.startsWith(other.fullPath);
    }

    public boolean endsWith(@NotNull VirtualAddress other)
    {
        return this.fullPath.endsWith(other.fullPath);
    }

    public static class KeyAddrDeserializer extends KeyDeserializer
    {
        @Override
        public VirtualAddress deserializeKey(String key, DeserializationContext ctxt)
        {
            return VirtualAddress.parse(key, false);
        }
    }

    public static class Serializer extends StdSerializer<VirtualAddress>
    {
        public Serializer()
        {
            super(VirtualAddress.class);
        }

        @Override
        public void serialize(VirtualAddress value, JsonGenerator gen, SerializerProvider provider) throws IOException
        {
            gen.writeString(value.fullPath);
        }
    }

    public static class Deserializer extends StdDeserializer<VirtualAddress>
    {
        public Deserializer()
        {
            super(VirtualAddress.class);
        }

        @Override
        public VirtualAddress deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            return VirtualAddress.parse(p.getValueAsString(), false);
        }
    }
}
