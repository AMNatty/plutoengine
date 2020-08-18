package cz.tefek.io.modloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cz.tefek.io.asl.resource.ResourceHelper;
import cz.tefek.io.pluto.debug.Logger;

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
        Logger.log("[Pluto Mod Loader] Looking for installed mods.");

        File modDir = new File(ResourceHelper.GLOBAL_ROOT + "mods/");

        if (!modDir.exists())
        {
            modDir.mkdir();

            Logger.log("[Pluto Mod Loader] No mod found.");

            return;
        }

        mods = new ArrayList<>(Arrays.asList(modDir.list()));

        if (mods.size() == 0)
        {
            Logger.log("[Pluto Mod Loader] No mod found.");
        }
        else
        {
            Logger.log("[Pluto Mod Loader] Found " + mods.size() + " mod(s) to load.");

            try
            {
                loadAll();
            }
            catch (Exception e)
            {
                Logger.logException(e);
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
                Logger.log("[Pluto Mod Loader] There is one mod to load.");
            }
            else
            {
                Logger.log("[Pluto Mod Loader] There are " + mods.size() + " mods to load.");
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

                    Logger.log("[Pluto Mod Loader] Loaded mod jar file: " + modname + "/mod.jar");
                    i++;
                }
                else
                {
                    Logger.log("[Pluto Mod Loader] Failed to load mod jar file: " + modname + ".");
                    Logger.log("[Pluto Mod Loader] Reason: Missing mod.jar file.");
                }
            }

            if (i < 1 || i == 0)
            {
                System.out.println("[Pluto Mod Loader] Loading mods complete, loaded " + i + " mods.");
            }
            else
            {
                System.out.println("[Pluto Mod Loader] Loading mods complete, loaded " + i + " mod.");
            }
        }
        else
        {
            Logger.log("[Pluto Mod Loader] There aren't any mods, skipping initialising phase.");
        }
    }
}
