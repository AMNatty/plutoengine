package org.plutoengine.resource.filesystem;

import org.jetbrains.annotations.NotNull;
import org.plutoengine.PlutoLocal;
import org.plutoengine.address.VirtualAddress;
import org.plutoengine.mod.Mod;
import org.plutoengine.mod.ModLoader;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResourceFileSystemProvider extends FileSystemProvider
{
    record ResourceFileSystemID(
        Mod mod,
        VirtualAddress rootAddress
    )
    {

    }

    static final String ENV_PLUTO_MOD = "pluto-mod";
    static final String ENV_PLUTO_ADDRESS = "pluto-address";
    static final String ENV_PLUTO_FS_TYPE = "pluto-rfs-type";

    private final Map<ResourceFileSystemID, URI> fsURIs = new HashMap<>();
    private final Map<URI, Integer> refCount = new HashMap<>();
    private final Map<URI, ResourceFileSystem> fileSystems = new HashMap<>();

    /**
     * @deprecated Do not call directly!
     */
    @Deprecated
    public ResourceFileSystemProvider()
    {

    }

    @Override
    public String getScheme()
    {
        return ResourceManager.URI_SCHEME;
    }

    @Override
    public synchronized ResourceFileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException
    {
        var type = (EnumBackingFileSystem) env.get(ENV_PLUTO_FS_TYPE);

        var fs = type.create(uri);

        var mod = (Mod) env.get(ENV_PLUTO_MOD);
        var address = (VirtualAddress) env.get(ENV_PLUTO_ADDRESS);

        var rootPath = type.createRootPath(uri, fs);

        var rfs = new ResourceFileSystem(this, mod, address, type, uri, fs, rootPath);

        this.fileSystems.put(uri, rfs);
        this.refCount.compute(uri, (k, v) -> v != null ? v + 1 : 1);

        var rfsID = new ResourceFileSystemID(mod, address);
        fsURIs.put(rfsID, uri);

        return rfs;
    }

    synchronized void destroyFileSystem(URI uri) throws IOException
    {
        if (this.refCount.computeIfPresent(uri, (k, v) -> v > 1 ? v - 1 : null) != null)
            return;

        var fs = this.fileSystems.get(uri);

        if (fs != null)
        {
            this.fsURIs.remove(new ResourceFileSystemID(fs.getMod(), fs.getAddress()));

            if (fs != FileSystems.getDefault())
                fs.close();
        }

        this.fileSystems.remove(uri);
    }

    @Override
    public synchronized ResourceFileSystem getFileSystem(URI uri)
    {
        return this.fileSystems.get(uri);
    }

    @Override
    public synchronized ResourcePath getPath(@NotNull URI uri)
    {
        var urp = ResourcePathParser.parse(uri);

        var modLoader = PlutoLocal.components().getComponent(ModLoader.class);

        var mod = modLoader.getModByID(urp.modID());

        if (mod.isEmpty())
            throw new IllegalArgumentException("No such mod exists!");

        var rfsID = new ResourceFileSystemID(mod.get(), urp.containerID());

        var rfsURI = this.fsURIs.get(rfsID);

        if (rfsURI == null)
            throw new IllegalArgumentException("No such mod ID or resource file system registered!");

        var rfs = this.fileSystems.get(rfsURI);

        assert rfs != null;

        return rfs.getPath(urp);
    }

    @Override
    public synchronized SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.newByteChannel(backingPath, options, attrs);
    }

    @Override
    public synchronized DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException
    {
        if (!(dir instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.newDirectoryStream(backingPath, filter);
    }

    @Override
    public synchronized void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
    {
        if (!(dir instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        backingProvider.createDirectory(backingPath, attrs);
    }

    @Override
    public synchronized void delete(Path path) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        backingProvider.delete(backingPath);
    }

    @Override
    public synchronized void copy(Path source, Path target, CopyOption... options) throws IOException
    {
        if (!(source instanceof ResourcePath rpSrc))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        if (!(target instanceof ResourcePath rpTgt))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingSourcePath = rpSrc.getBackingPath();
        var backingSourceFileSystem = backingSourcePath.getFileSystem();
        var backingSourceProvider = backingSourceFileSystem.provider();

        var backingTargetPath = rpTgt.getBackingPath();

        backingSourceProvider.copy(backingSourcePath, backingTargetPath, options);
    }

    @Override
    public synchronized void move(Path source, Path target, CopyOption... options) throws IOException
    {
        if (!(source instanceof ResourcePath rpSrc))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        if (!(target instanceof ResourcePath rpTgt))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingSourcePath = rpSrc.getBackingPath();
        var backingSourceFileSystem = backingSourcePath.getFileSystem();
        var backingSourceProvider = backingSourceFileSystem.provider();

        var backingTargetPath = rpTgt.getBackingPath();

        backingSourceProvider.move(backingSourcePath, backingTargetPath, options);
    }

    @Override
    public synchronized boolean isSameFile(Path path, Path path2) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        if (!(path2 instanceof ResourcePath rp2))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath2 = rp2.getBackingPath();

        return backingProvider.isSameFile(backingPath, backingPath2);
    }

    @Override
    public synchronized boolean isHidden(Path path) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.isHidden(backingPath);
    }

    @Override
    public synchronized FileStore getFileStore(Path path) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.getFileStore(backingPath);
    }

    @Override
    public synchronized void checkAccess(Path path, AccessMode... modes) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        backingProvider.checkAccess(backingPath, modes);
    }

    @Override
    public synchronized <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options)
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.getFileAttributeView(backingPath, type, options);
    }

    @Override
    public synchronized <A extends BasicFileAttributes > A readAttributes(Path path, Class < A > type, LinkOption... options) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.readAttributes(backingPath, type, options);
    }

    @Override
    public synchronized Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        return backingProvider.readAttributes(backingPath, attributes, options);
    }

    @Override
    public synchronized void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException
    {
        if (!(path instanceof ResourcePath rp))
            throw new IllegalArgumentException("Expected a path of type %s!".formatted(ResourcePath.class));

        var backingPath = rp.getBackingPath();
        var backingFileSystem = backingPath.getFileSystem();
        var backingProvider = backingFileSystem.provider();

        backingProvider.setAttribute(backingPath, attribute, value, options);
    }
}
