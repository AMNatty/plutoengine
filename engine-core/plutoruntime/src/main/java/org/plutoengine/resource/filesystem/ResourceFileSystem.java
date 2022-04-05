package org.plutoengine.resource.filesystem;

import org.jetbrains.annotations.NotNull;
import org.plutoengine.address.VirtualAddress;
import org.plutoengine.mod.Mod;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.List;
import java.util.Set;

final class ResourceFileSystem extends FileSystem implements Comparable<ResourceFileSystem>
{
    private final Mod mod;
    private final VirtualAddress address;

    private final URI uri;
    private final FileSystem fileSystemImpl;

    private final ResourceFileSystemProvider provider;

    private final ResourcePath root;

    private final EnumBackingFileSystem backingFileSystemType;
    private final Path backingRootPath;

    ResourceFileSystem(ResourceFileSystemProvider provider,
                       Mod mod,
                       VirtualAddress address,
                       EnumBackingFileSystem backingFileSystemType,
                       URI uri,
                       FileSystem backingFileSystem,
                       Path rootPath)
    {
        this.provider = provider;
        this.backingFileSystemType = backingFileSystemType;
        this.uri = uri;

        this.fileSystemImpl = backingFileSystem;
        this.backingRootPath = rootPath;

        this.mod = mod;
        this.address = address;

        var urp = new UnresolvedResourcePath(this.mod.getID(),
            this.getAddress(),
            false,
            VirtualAddress.ofRoot(),
            null);

        this.root = this.getPath(urp);
    }

    public Mod getMod()
    {
        return this.mod;
    }

    public VirtualAddress getAddress()
    {
        return this.address;
    }

    @Override
    public ResourceFileSystemProvider provider()
    {
        return this.provider;
    }

    @Override
    public void close() throws IOException
    {
        this.provider.destroyFileSystem(this.uri);
    }

    @Override
    public boolean isOpen()
    {
        return this.fileSystemImpl.isOpen();
    }

    @Override
    public boolean isReadOnly()
    {
        return this.fileSystemImpl.isReadOnly();
    }

    @Override
    public String getSeparator()
    {
        return Character.toString(VirtualAddress.TOKEN_PATH_SEPARATOR);
    }

    public ResourcePath getRoot()
    {
        return this.root;
    }

    @Override
    public Iterable<Path> getRootDirectories()
    {
        return List.of(this.getRoot());
    }

    @Override
    public Iterable<FileStore> getFileStores()
    {
        return this.fileSystemImpl.getFileStores();
    }

    @Override
    public Set<String> supportedFileAttributeViews()
    {
        return this.fileSystemImpl.supportedFileAttributeViews();
    }

    @Override
    public @NotNull ResourcePath getPath(@NotNull String first, String @NotNull... more)
    {
        var separator = this.getSeparator();

        var pathBld = new StringBuilder(first);

        if (more.length > 0)
        {
            pathBld.append(VirtualAddress.TOKEN_PATH_SEPARATOR);
            pathBld.append(String.join(separator, more));
        }

        var pathStr = pathBld.toString();
        var raw = pathStr.replace("..", ".");

        var urp = ResourcePathParser.parse(raw);

        return this.getPath(urp);
    }

    public ResourcePath getPath(UnresolvedResourcePath urp)
    {
        return new ResourcePath(this,
            urp.pathAddress(),
            urp.extension(),
            this.backingRootPath.resolve(this.backingFileSystemType.createBackingPath(this.fileSystemImpl, urp)));
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern)
    {
        return this.fileSystemImpl.getPathMatcher(syntaxAndPattern);
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService()
    {
        return this.fileSystemImpl.getUserPrincipalLookupService();
    }

    @Override
    public WatchService newWatchService() throws IOException
    {
        return this.fileSystemImpl.newWatchService();
    }

    @Override
    public int compareTo(ResourceFileSystem o)
    {
        var modDiff = this.mod.compareTo(o.mod);

        if (modDiff != 0)
            return modDiff;

        return this.address.compareTo(o.address);
    }

    @Override
    public String toString()
    {
        return this.mod + "+" + this.address;
    }
}
