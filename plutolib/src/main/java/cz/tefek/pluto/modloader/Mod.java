package cz.tefek.pluto.modloader;

import cz.tefek.pluto.io.asl.resource.ResourceSubscriber;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.pluto.pp.PlutoPackage;

/**
 * Mod object. Can be used to create a {@link ResourceSubscriber}.
 * {@link ModLoaderCore} automatically creates a Mod object for each class
 * annotated by {@link ModEntry @ModEntry} (assuming it is registered or class
 * loaded by ModClassLoader, which auto-detects and registers {@link ModEntry
 * ModEntries}).
 *
 * @see <a href="http://pluto.tefek.cz/mod/dev/">PlutoModLoader tutorial</a> for
 *      more information.
 *
 * @author 493msi
 */
public class Mod extends PlutoPackage
{
    private Class<?> mainClass;
    private Object instance;

    private boolean clientSupport;
    private boolean serverSupport;

    private ResourceSubscriber defaultResourceSubscriber;

    private String rootDataFolder;

    Mod(Class<?> mainClass, String rootDataFolder)
    {
        super(extractModID(mainClass));

        if (mainClass != null)
        {
            ModEntry modInterface = mainClass.getDeclaredAnnotation(ModEntry.class);

            if (modInterface != null)
            {
                this.mainClass = mainClass;
                this.name = modInterface.displayName().isBlank() ? modInterface.modid() : modInterface.displayName();
                this.author = modInterface.author();
                this.version = modInterface.version();
                this.build = modInterface.build();
                this.earliestCompatibleBuild = modInterface.earliestCompatibleBuild();
                this.dependencies = modInterface.dependencies();
                this.description = modInterface.description();
                this.iconURL = modInterface.iconURL();

                this.rootDataFolder = rootDataFolder;
                this.defaultResourceSubscriber = new ResourceSubscriber(this, rootDataFolder);

                this.clientSupport = modInterface.clientSupport();
                this.serverSupport = modInterface.serverSupport();
            }
        }
    }

    private static String extractModID(Class<?> mainClass)
    {
        if (mainClass != null)
        {
            ModEntry modInterface = mainClass.getDeclaredAnnotation(ModEntry.class);

            if (modInterface != null)
            {
                return modInterface.modid();
            }
        }

        return null;
    }

    Class<?> getMainClass()
    {
        return this.mainClass;
    }

    Object getClassInstance() throws ReflectiveOperationException
    {
        if (this.instance == null)
        {
            Logger.log("|Pluto Mod Loader| Loading mod instance: " + this.name);
            this.instance = this.mainClass.getDeclaredConstructor().newInstance();
        }

        return this.instance;
    }

    public String getModID()
    {
        return this.id;
    }

    public String getModName()
    {
        return this.name;
    }

    public String getModAuthor()
    {
        return this.author;
    }

    public String getModVersion()
    {
        return this.version;
    }

    public int getModBuild()
    {
        return this.build;
    }

    public boolean isClientSupported()
    {
        return this.clientSupport;
    }

    public boolean isServerSupported()
    {
        return this.serverSupport;
    }

    public ResourceSubscriber getDefaultResourceSubscriber()
    {
        return this.defaultResourceSubscriber;
    }

    public void setRootDataFolder(String rootDataFolder)
    {
        this.rootDataFolder = rootDataFolder;
        this.defaultResourceSubscriber = new ResourceSubscriber(this, rootDataFolder);
    }

    public String getRootDataFolder()
    {
        return this.rootDataFolder;
    }
}
