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

import org.plutoengine.address.VirtualAddress;
import org.plutoengine.mod.Mod;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

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
                for (FileSystemProvider provider : FileSystemProvider.installedProviders())
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
