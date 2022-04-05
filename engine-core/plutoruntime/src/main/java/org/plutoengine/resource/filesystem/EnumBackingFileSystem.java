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
        (uri, fileSystem) -> fileSystem.getPath(uri.getPath()),
        (fs, address, ext) -> {
            var sep = fs.getSeparator();
            var rootOffs = address.getRootOffset();

            var path = (".." + sep).repeat(rootOffs) + String.join(sep, address.getComponents());

            if (ext != null)
                return fs.getPath(path + "." + ext);

            return fs.getPath(path);
        }),
    FS_ZIP("zip",
        uri -> FileSystems.newFileSystem(uri, Map.of()),
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
