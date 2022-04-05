package org.plutoengine.resource.filesystem;

import org.plutoengine.address.VirtualAddress;
import org.plutoengine.mod.Mod;

import javax.annotation.concurrent.ThreadSafe;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

@ThreadSafe
public class ResourceManager implements Closeable
{
    public static final Path GLOBAL_ROOT = Path.of("");
    public static final Path MOD_DIRECTORY = GLOBAL_ROOT.resolve("mods");
    public static final String URI_SCHEME = "pluto+asl";
    public static final Path MOD_MANIFEST_PATH = Path.of("info.json");

    private static ResourceFileSystemProvider RESOURCE_FILE_SYSTEM_PROVIDER = null;

    public static ResourceFileSystemProvider provider()
    {
        if (RESOURCE_FILE_SYSTEM_PROVIDER == null)
        {
            try
            {
                var loader = ServiceLoader.load(FileSystemProvider.class, ResourceFileSystemProvider.class.getClassLoader());

                for (FileSystemProvider provider : loader)
                {
                    if (provider.getScheme().equals(URI_SCHEME))
                    {
                        RESOURCE_FILE_SYSTEM_PROVIDER = (ResourceFileSystemProvider) provider;
                        break;
                    }
                }
            }
            catch (ProviderNotFoundException | ServiceConfigurationError e)
            {
                throw new RuntimeException(e);
            }
        }

        return RESOURCE_FILE_SYSTEM_PROVIDER;
    }

    private final Mod mod;
    private final Path modDirectory;
    private final Map<VirtualAddress, ResourceFileSystem> fileSystems;

    public ResourceManager(Mod mod)
    {
        this.mod = mod;
        var modID = this.mod.getID();
        this.modDirectory = MOD_DIRECTORY.resolve(modID.toString());
        this.fileSystems = new HashMap<>();
    }

    public void addFileSystem(VirtualAddress fileSystemAddress, EnumBackingFileSystem type) throws IOException
    {
        var manifest = mod.getManifest();
        var resourceRoots = manifest.resourceRoots();
        var info = resourceRoots.get(fileSystemAddress);

        var envMap = new HashMap<String, Object>();
        envMap.put(ResourceFileSystemProvider.ENV_PLUTO_MOD, mod);
        envMap.put(ResourceFileSystemProvider.ENV_PLUTO_ADDRESS, fileSystemAddress);
        envMap.put(ResourceFileSystemProvider.ENV_PLUTO_FS_TYPE, type);

        var fsPath = this.modDirectory.resolve(info.path());
        var rfs = provider().newFileSystem(fsPath.toUri(), envMap);

        this.fileSystems.put(fileSystemAddress, rfs);
    }

    public Path getModDirectory()
    {
        return this.modDirectory;
    }

    public ResourceFileSystem getFileSystem(VirtualAddress fileSystemAddress)
    {
        return this.fileSystems.get(fileSystemAddress);
    }

    public Path getPath(VirtualAddress fileSystemID, VirtualAddress address, String ext)
    {
        var fs = this.fileSystems.get(fileSystemID);
        return fs.getPath(new UnresolvedResourcePath(this.mod.getID(), fs.getAddress(), address.isRelative(), address, ext));
    }

    public Path getPath(String path)
    {
        var urp = ResourcePathParser.parse(this.mod.getID() + "+" + path);
        return this.getPath(urp);
    }

    private ResourcePath getPath(UnresolvedResourcePath urp)
    {
        var fs = this.fileSystems.get(urp.containerID());
        return fs.getPath(urp);
    }

    @Override
    public void close() throws IOException
    {
        for (var fs : this.fileSystems.values())
            fs.close();

        this.fileSystems.clear();
    }
}
