package cz.tefek.pluto.modloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import cz.tefek.pluto.io.asl.resource.ResourceHelper;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

/**
 * Class-loads all valid mods. The only requirement for the mod is to have a
 * mod.jar file inside the base folder (for example
 * <i>mods/spaghetti/mod.jar</i>).
 *
 * @author 493msi
 */
public class ModClassLoader
{
    public static ArrayList<String> mods;

    public static void loadJars()
    {
        Logger.log(SmartSeverity.MODULE, "Looking for installed mods.");

        File modDir = new File(ResourceHelper.GLOBAL_ROOT + "mods/");

        if (!modDir.exists())
        {
            modDir.mkdir();

            Logger.log(SmartSeverity.MODULE, " No mod found.");

            return;
        }

        mods = new ArrayList<>(Arrays.asList(modDir.list()));

        if (mods.size() == 0)
        {
            Logger.log(SmartSeverity.MODULE, "No mod found.");
        }
        else
        {
            Logger.log(SmartSeverity.MODULE, "Found " + mods.size() + " mod(s) to load.");

            try
            {
                loadAll();
            }
            catch (Exception e)
            {
                Logger.log(e);
            }
        }
    }

    private static void loadAll() throws Exception
    {
        int i = 0;

        if (mods.size() > 0)
        {
            if (mods.size() == 1)
            {
                Logger.log(SmartSeverity.MODULE, "There is one mod to load.");
            }
            else
            {
                Logger.log(SmartSeverity.MODULE, "There are " + mods.size() + " mods to load.");
            }

            for (String modname : mods)
            {
                var modFolder = ResourceHelper.GLOBAL_ROOT + "mods/" + modname;
                var dataDir = modFolder + "/data";

                if (new File(modFolder + "/mod.jar").exists())
                {
                    var dataDirFile = new File(dataDir);

                    if (!dataDirFile.isDirectory())
                    {
                        dataDirFile.mkdirs();
                    }

                    String pathToJar = modFolder + "/mod.jar";

                    JarFile jarFile = new JarFile(pathToJar);
                    Enumeration<JarEntry> e = jarFile.entries();

                    URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };

                    URLClassLoader sysLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());

                    while (e.hasMoreElements())
                    {
                        JarEntry je = e.nextElement();

                        if (je.isDirectory())
                        {
                            continue;
                        }

                        // Not sure what to do with non-java files.
                        // They are ignored (for now).
                        if (!je.getName().endsWith(".class"))
                        {
                            continue;
                        }

                        String className = je.getName().replaceAll("\\.class$", "").replace('/', '.');

                        Class<?> modClass = sysLoader.loadClass(className);

                        if (modClass.getDeclaredAnnotation(ModEntry.class) != null)
                        {
                            ModLoaderCore.registerMod(modClass, dataDir);
                        }
                    }

                    jarFile.close();

                    Logger.log(SmartSeverity.MODULE_PLUS, "Loaded mod jar file: " + modname + "/mod.jar");
                    i++;
                }
                else
                {
                    Logger.log(SmartSeverity.MODULE_WARNING, "Failed to load mod jar file: " + modname + ".");
                    Logger.log(SmartSeverity.MODULE_WARNING, "Reason: Missing mod.jar file.");
                }
            }

            if (i < 1 || i == 0)
            {
                Logger.log(SmartSeverity.MODULE, "Loading mods complete, loaded " + i + " mods.");
            }
            else
            {
                Logger.log(SmartSeverity.MODULE, "Loading mods complete, loaded " + i + " mod.");
            }
        }
        else
        {
            Logger.log(SmartSeverity.MODULE, "There aren't any mods, skipping initialising phase.");
        }
    }
}
