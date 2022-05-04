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

package org.plutoengine.resource.filesystem;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.apache.commons.lang3.function.TriFunction;
import org.plutoengine.address.VirtualAddress;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@JsonSerialize(converter = EnumBackingFileSystem.Serializer.class)
@JsonDeserialize(converter = EnumBackingFileSystem.Deserializer.class)
public enum EnumBackingFileSystem
{
    FS_DIRECTORY("open",
        uri -> FileSystems.getDefault(),
        (uri, fileSystem) -> fileSystem.provider().getPath(uri),
        (fs, address, ext) -> {
            var sep = fs.getSeparator();
            var rootOffs = address.getRootOffset();

            var path = (".." + sep).repeat(rootOffs) + String.join(sep, address.getComponents());

            if (ext != null)
                return fs.getPath(path + "." + ext);

            return fs.getPath(path);
        }),
    FS_ZIP("zip",
        uri -> {
            var provider = FileSystems.getDefault().provider();
            return FileSystems.newFileSystem(provider.getPath(uri), Map.of(), null);
        },
        (uri, fileSystem) -> fileSystem.getPath("/"),
        (fs, address, ext) -> {
            var sep = fs.getSeparator();
            var rootOffs = address.getRootOffset();

            var path = (".." + sep).repeat(rootOffs) + String.join(sep, address.getComponents());

            if (ext != null)
                return fs.getPath(path + "." + ext);

            return fs.getPath(path);
        });

    @FunctionalInterface
    public interface FileSystemCreateFunc
    {
        FileSystem create(URI uri) throws IOException;
    }

    private static final Map<String, EnumBackingFileSystem> LOOKUP = new HashMap<>();

    static
    {
        for (var item : values())
            LOOKUP.put(item.id, item);
    }

    private final String id;
    private final FileSystemCreateFunc createFunction;
    private final BiFunction<URI, FileSystem, Path> getRootFunction;
    // (input filesystem, path address, extension) -> backing path
    private final TriFunction<FileSystem, VirtualAddress, String, Path> backingPathConverter;

    EnumBackingFileSystem(String id, FileSystemCreateFunc createFunc, BiFunction<URI, FileSystem, Path> getRootFunction, TriFunction<FileSystem, VirtualAddress, String, Path> backingPathConverter)
    {
        this.id = id;
        this.createFunction = createFunc;
        this.getRootFunction = getRootFunction;
        this.backingPathConverter = backingPathConverter;
    }

    public FileSystem create(URI uri) throws IOException
    {
        return this.createFunction.create(uri);
    }

    public Path createRootPath(URI uri, FileSystem fs)
    {
        return this.getRootFunction.apply(uri, fs);
    }

    public String getID()
    {
        return this.id;
    }

    public Path createBackingPath(FileSystem fs, UnresolvedResourcePath urp)
    {
        return this.backingPathConverter.apply(fs, urp.pathAddress(), urp.extension());
    }

    public static EnumBackingFileSystem getByID(String id)
    {
        return LOOKUP.get(id);
    }

    public static class Serializer extends StdConverter<EnumBackingFileSystem, String>
    {
        @Override
        public String convert(EnumBackingFileSystem value)
        {
            return value.id;
        }
    }

    public static class Deserializer extends StdConverter<String, EnumBackingFileSystem>
    {
        @Override
        public EnumBackingFileSystem convert(String value)
        {
            return Objects.requireNonNull(LOOKUP.get(value));
        }
    }
}
