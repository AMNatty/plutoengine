package org.plutoengine.mod;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.plutoengine.address.VirtualAddress;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;
import org.plutoengine.resource.filesystem.ResourceManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * Mod object.
 *
 * <p>
 * {@link ModLoader} automatically creates a Mod object for each class
 * annotated by {@link ModEntry @ModEntry} (assuming it is registered or class
 * loaded by ModClassLoader, which auto-detects and registers {@link ModEntry
 * ModEntries}).
 * </p>
 *
 * @see <a href="https://plutoengine.org/mod/dev/">PlutoModLoader tutorial</a> for
 *      more information.
 *
 * @since 20.2.0.0-alpha.3
 *
 * @author 493msi
 */
public class Mod implements Comparable<Mod>
{
    private final VirtualAddress id;
    private final String version;
    private final Class<?>[] dependencies;

    private Class<?> mainClass;

    private ModManifest manifest;

    private ResourceManager resourceManager;

    public Mod(VirtualAddress id, String version, Class<?>[] dependencies)
    {
        this.id = id;
        this.version = version;
        this.dependencies = dependencies;
    }

    static Mod from(VirtualAddress modID, Class<?>[] dependencies, String version, Class<?> mainClass)
    {
        var mod = new Mod(modID, version, dependencies);
        mod.mainClass = mainClass;

        var modIDStr = mod.id.toString();

        var modDir = ResourceManager.MOD_DIRECTORY.resolve(modIDStr);
        var modManifestFile = modDir.resolve(ResourceManager.MOD_MANIFEST_PATH);
        var modManifest = (ModManifest) null;

        if (!Files.isDirectory(modDir) || !Files.isRegularFile(modManifestFile))
        {
            Logger.logf(SmartSeverity.MODULE_WARNING, "Mod ID '%s' has a missing manifest (%s file located in the %s directory)," +
                                                      " it will not have any metadata attached to it.%n", modIDStr, ResourceManager.MOD_MANIFEST_PATH, modDir);
        }
        else
        {
            try (var br = Files.newBufferedReader(modManifestFile))
            {
                var om = new ObjectMapper();
                modManifest = om.readValue(br, ModManifest.class);
            }
            catch (Exception e)
            {
                Logger.logf(SmartSeverity.MODULE_ERROR, "Mod ID '%s' has a broken manifest (%s file located in the %s directory)," +
                                                        " it will not have any metadata attached to it.%n", modIDStr, ResourceManager.MOD_MANIFEST_PATH, modDir);

                Logger.log(e);
            }
        }

        mod.manifest = Objects.requireNonNullElseGet(modManifest, () -> new ModManifest(modID.toString(), "", "", Map.of()));

        mod.resourceManager = new ResourceManager(mod);

        mod.manifest.resourceRoots().forEach((rfsID, rfsData) -> {
            try
            {
                mod.resourceManager.addFileSystem(rfsID, rfsData.type());
            }
            catch (IOException e)
            {
                Logger.logf(SmartSeverity.MODULE_ERROR, "Could not open the file system '%s+%s'!", modIDStr, rfsID);

                Logger.log(e);
            }
        });

        return mod;
    }

    public VirtualAddress getID()
    {
        return this.id;
    }

    public Class<?> getMainClass()
    {
        return this.mainClass;
    }

    public String getVersion()
    {
        return this.version;
    }

    public Class<?>[] getDependencies()
    {
        return this.dependencies;
    }

    public ModManifest getManifest()
    {
        return this.manifest;
    }

    public ResourceManager getResourceManager()
    {
        return this.resourceManager;
    }

    public Path getResource(String path)
    {
        if (!path.contains("$"))
            path = "default$" + path;

        return this.resourceManager.getPath(path);
    }

    @Override
    public int compareTo(Mod o)
    {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString()
    {
        return this.id.toString();
    }
}

