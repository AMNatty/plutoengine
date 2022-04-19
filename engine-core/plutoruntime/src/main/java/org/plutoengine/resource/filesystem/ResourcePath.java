package org.plutoengine.resource.filesystem;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.plutoengine.address.VirtualAddress;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;

/**
 * pluto+asl://modID+resource.root$absolute.path.to.item[#extension]
 * pluto+asl://!relative.path.to.item[#extension]
 * pluto+asl://!^.path.to.item[#extension]
 *
 * modID                   unique mod identifier
 * assetContainerID        asset container (optional, default asset pack if unspecified)
 *
 * $                       absolute path
 * !                       relative path
 *
 * .                       path separator
 * ^                       go up one directory
 *
 * #extension              extension separator (optional)
 *
 * @author 493msi
 *
 */
final class ResourcePath implements Path
{
    public static final int MAX_EXTENSION_CHARACTERS = 16;

    private static final Comparator<ResourceFileSystem> FILE_SYSTEM_COMPARATOR = Comparator.nullsLast(Comparator.naturalOrder());

    private final VirtualAddress address;
    private final String extension;
    private final ResourceFileSystem fileSystem;
    private final Path backingPath;

    private String strRepresentation;

    ResourcePath(ResourceFileSystem fileSystem, VirtualAddress address, String extension, Path backingPath)
    {
        this.fileSystem = fileSystem;
        this.address = address;
        this.extension = extension;
        this.backingPath = backingPath;
    }

    public Path getBackingPath()
    {
        return this.backingPath;
    }

    @Override
    public ResourceFileSystem getFileSystem()
    {
        if (this.fileSystem == null)
            throw new IllegalStateException("This relative ResourcePath does not have an assigned file system, try using ofFileSystem(ResourceFileSystem) first.");

        return this.fileSystem;
    }

    @Override
    public boolean isAbsolute()
    {
        return !this.address.isRelative();
    }

    @Override
    public ResourcePath getRoot()
    {
        return this.getFileSystem().getRoot();
    }

    @Override
    public ResourcePath getFileName()
    {
        if (this.address.isEmpty())
            return null;

        return new ResourcePath(this.fileSystem, this.address.getName(this.address.getNameCount() - 1), this.extension, this.backingPath.getFileName());
    }

    @Override
    public ResourcePath getParent()
    {
        if (this.getNameCount() == 0 && this.isAbsolute())
            return null;

        return new ResourcePath(this.fileSystem, this.address.getParent(), null, this.backingPath.getParent());
    }

    @Override
    public int getNameCount()
    {
        return this.address.getNameCount();
    }

    @Override
    public @NotNull ResourcePath getName(int index)
    {
        if (index == 0 && this.address.isEmpty())
            return this;

        return new ResourcePath(
            this.fileSystem,
            this.address.getName(index),
            this.address.getNameCount() == index ? this.extension : null,
            this.backingPath.getName(index));
    }

    @Override
    public @NotNull ResourcePath subpath(int beginIndex, int endIndex)
    {
        return new ResourcePath(
            this.fileSystem,
            this.address.subAddress(beginIndex, endIndex),
            this.address.getNameCount() == endIndex ? this.extension : null,
            this.backingPath.subpath(beginIndex, endIndex));
    }

    @Override
    public boolean startsWith(@NotNull Path other)
    {
        if (!(other instanceof ResourcePath rp))
            throw new IllegalArgumentException("other must be of type %s!".formatted(ResourcePath.class));

        return this.toString()
                   .startsWith(rp.toString());
    }

    @Override
    public boolean endsWith(@NotNull Path other)
    {
        if (!(other instanceof ResourcePath rp))
            throw new IllegalArgumentException("other must be of type %s!".formatted(ResourcePath.class));

        return this.toString()
                   .endsWith(rp.toString());
    }

    @Override
    public ResourcePath normalize()
    {
        // Resource paths are normalized by design
        return this;
    }

    @Override
    public ResourcePath resolve(@NotNull Path other)
    {
        if (!(other instanceof ResourcePath rp))
            throw new IllegalArgumentException("other must be of type %s!".formatted(ResourcePath.class));

        if (rp.isAbsolute())
            return rp;

        var addr = this.address.resolve(rp.address);

        var urp = new UnresolvedResourcePath(
            this.fileSystem.getMod().getID(),
            this.fileSystem.getAddress(),
            addr.isRelative(),
            addr,
            rp.extension
        );

        return this.fileSystem.getPath(urp);
    }

    @Override
    public @NotNull ResourcePath resolve(@NotNull String other)
    {
        // Unless we are SURE "other" is absolute, we consider it to be relative

        if (!other.contains(Character.toString(ResourcePathParser.TOKEN_ABSOLUTE_SEPARATOR)))
            other = StringUtils.prependIfMissing(other, Character.toString(ResourcePathParser.TOKEN_RELATIVE_SEPARATOR));

        return this.resolve(this.getFileSystem().getPath(other));
    }

    @Override
    public @NotNull ResourcePath relativize(@NotNull Path other)
    {
        if (!(other instanceof ResourcePath rp))
            throw new IllegalArgumentException("other must be of type %s!".formatted(ResourcePath.class));


        if (this.isAbsolute() != rp.isAbsolute())
            throw new IllegalArgumentException("Cannot relativize a %s when only one of the inputs is absolute!".formatted(ResourcePath.class));

        if (this.isAbsolute() && !this.fileSystem.equals(rp.fileSystem))
            throw new IllegalArgumentException("Cannot relativize towards an absolute %s from a different file system!".formatted(ResourcePath.class));

        var addr = this.address.relativize(address);

        var urp = new UnresolvedResourcePath(
            this.fileSystem.getMod().getID(),
            this.fileSystem.getAddress(),
            addr.isRelative(),
            addr,
            rp.extension
        );

        return this.fileSystem.getPath(urp);
    }

    @Override
    public @NotNull URI toUri()
    {
        try
        {
            return new URI(ResourceManager.URI_SCHEME, this.toStringNoFragment(), this.extension);
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ResourcePath toAbsolutePath()
    {
        if (this.isAbsolute())
            return this;

        // Convert to an absolute path, basically resolving against the file system's root
        return this.getFileSystem()
                   .getRoot()
                   .resolve(this);
    }

    @Override
    public @NotNull ResourcePath toRealPath(LinkOption @NotNull... options)
    {
        return this.toAbsolutePath();
    }

    @Override
    public @NotNull WatchKey register(@NotNull WatchService watcher, WatchEvent.Kind<?>@NotNull[] events, WatchEvent.Modifier... modifiers) throws IOException
    {
        return this.backingPath.register(watcher, events, modifiers);
    }

    @Override
    public int compareTo(@NotNull Path other)
    {
        if (!(other instanceof ResourcePath rp))
            throw new UnsupportedOperationException();

        var containerDiff = Objects.compare(this.fileSystem, rp.fileSystem, FILE_SYSTEM_COMPARATOR);

        if (containerDiff != 0)
            return containerDiff;

        var addressDif = this.address.compareTo(rp.address);

        if (addressDif != 0)
            return addressDif;

        return this.extension.compareTo(rp.extension);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof ResourcePath rp))
            return false;
        
        return Objects.equals(this.address, rp.address) &&
               Objects.equals(this.fileSystem, rp.fileSystem) &&
               Objects.equals(this.extension, rp.extension);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.address, this.fileSystem, this.extension);
    }

    private String toStringNoFragment()
    {
        if (this.isAbsolute())
            return this.fileSystem.toString() +
                   Character.toString(ResourcePathParser.TOKEN_ABSOLUTE_SEPARATOR) +
                   this.address.toString();
        else
            return Character.toString(ResourcePathParser.TOKEN_RELATIVE_SEPARATOR) +
                   this.address.toString();

    }

    @Override
    public @NotNull String toString()
    {
        if (this.strRepresentation == null)
        {
            if (this.extension != null)
                this.strRepresentation = this.toStringNoFragment() +
                                         Character.toString(ResourcePathParser.TOKEN_FILE_EXTENSION_SEPARATOR) +
                                         this.extension;
            else
                this.strRepresentation = this.toStringNoFragment();

        }

        return this.strRepresentation;
    }
}
