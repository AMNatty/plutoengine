package cz.tefek.io.modloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cz.tefek.io.asl.resource.ResourceHelper;
import cz.tefek.io.modloader.event.ModLoad;
import cz.tefek.io.modloader.event.ModLoadEvent;
import cz.tefek.io.modloader.event.ModPostLoad;
import cz.tefek.io.modloader.event.ModPostLoadEvent;
import cz.tefek.io.modloader.event.ModPreLoad;
import cz.tefek.io.modloader.event.ModPreLoadEvent;
import cz.tefek.io.modloader.event.ModUnload;
import cz.tefek.io.modloader.event.ModUnloadEvent;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.SmartSeverity;
import cz.tefek.pluto.eventsystem.staticmode.StaticPlutoEventManager;

public class ModLoaderCore
{
    public static final String MOD_ID_STRING_PATTERN = "[a-z0-9_]+";
    public static final String FULL_MOD_ID_STRING_PATTERN = "^" + MOD_ID_STRING_PATTERN + "$";

    static ModLoadingPhase loadingPhase = ModLoadingPhase.WAITING;

    private static final List<Mod> modArchive = new ArrayList<>();

    private static final LinkedList<Mod> loadBuffer = new LinkedList<>();

    public static void registerMod(Class<?> modClass, String modDataRoot)
    {
        if (loadingPhase != ModLoadingPhase.WAITING && loadingPhase != ModLoadingPhase.CLASSLOADING)
        {
            Logger.log(SmartSeverity.ERROR, "Cannot register mod during loading phase " + loadingPhase.name() + "!");
            return;
        }

        if (getModByMainClass(modClass) != null)
        {
            Logger.log(SmartSeverity.WARNING, "[Pluto Mod Loader] Mod class " + modClass.getCanonicalName() + " is already registered, skipping it.");
            return;
        }

        if (modDataRoot == null)
        {
            modDataRoot = ResourceHelper.DEFAULT_RESOURCE_ROOT;
        }

        var registeredMod = loadBuffer.stream().filter(presentMod -> presentMod.getMainClass().equals(modClass)).findAny();

        if (registeredMod.isPresent())
        {
            if (modDataRoot != null)
            {
                var mod = registeredMod.get();

                mod.setRootDataFolder(modDataRoot);
            }

            return;
        }

        Mod mod = new Mod(modClass, modDataRoot);

        if (!mod.getModID().matches(FULL_MOD_ID_STRING_PATTERN))
        {
            Logger.log(SmartSeverity.WARNING, "[Pluto Mod Loader] Modid " + mod.getModID() + " contains invalid characters (or none at all), mod will not be loaded.");
            Logger.log(SmartSeverity.WARNING, "[Pluto Mod Loader] Only lowercase letters (a to z) and numbers (0 to 9) are allowed characters.");
            return;
        }

        var deps = mod.getDependencies();

        for (var dependency : deps)
        {
            var registeredDependency = loadBuffer.stream().filter(presentMod -> presentMod.getMainClass().equals(dependency)).findAny();

            if (registeredDependency.isPresent())
            {
                continue;
            }

            registerMod(dependency);
        }

        loadBuffer.add(mod);
    }

    public static void registerMod(Class<?> modClass)
    {
        registerMod(modClass, null);
    }

    public static List<Mod> getAllMods()
    {
        return Collections.unmodifiableList(modArchive);
    }

    public static Mod getModByID(String id)
    {
        for (Mod mod : modArchive)
        {
            if (mod.getModID().equals(id))
            {
                return mod;
            }
        }

        return null;
    }

    private static Mod getModByMainClass(Class<?> modClass)
    {
        for (Mod mod : modArchive)
        {
            if (modClass.equals(mod.getMainClass()))
            {
                return mod;
            }
        }

        return null;
    }

    public static void loadProcedure()
    {
        loadingPhase = ModLoadingPhase.PREPARING;

        StaticPlutoEventManager.registerEvent(ModPreLoad.class);
        StaticPlutoEventManager.registerEvent(ModLoad.class);
        StaticPlutoEventManager.registerEvent(ModPostLoad.class);
        StaticPlutoEventManager.registerEvent(ModUnload.class);

        Logger.log("[Pluto Mod Loader] Number of manually added mods: " + modArchive.size());
        loadingPhase = ModLoadingPhase.UPACKING;
        ModInstaller.unpackNewMods();
        loadingPhase = ModLoadingPhase.CLASSLOADING;
        ModClassLoader.loadJars();
        loadingPhase = ModLoadingPhase.INITIALIZING;

        while (loadBuffer.peek() != null)
        {
            var mod = loadBuffer.removeFirst();
            StaticPlutoEventManager.registerEventHandler(mod.getMainClass());
            modArchive.add(mod);
        }

        if (!modArchive.isEmpty())
        {
            Logger.log("[Pluto Mod Loader] Initializing mod(s)...");
            initMods();

            if (loadingPhase == ModLoadingPhase.FINISHING)
            {
                Logger.log("[Pluto Mod Loader] Initializing mod(s) finished.");
            }
            else
            {
                Logger.log("[Pluto Mod Loader] Initializing mod(s) was canceled due to error(s).");
            }
        }
    }

    static void initMods()
    {
        while (loadingPhase != ModLoadingPhase.CANCELED && loadingPhase != ModLoadingPhase.FINISHING)
        {
            switch (loadingPhase)
            {
                case INITIALIZING:
                    preLoadMods();
                    break;
                case PRELOADING:
                    loadMods();
                    break;
                case LOADING:
                    postLoadMods();
                    break;
                case POSTLOADING:
                    complete();
                    break;
                default:
                    break;
            }
        }
    }

    public static void unloadProcedure()
    {
        Logger.log("[Pluto Mod Loader] Unloading all mods.");
        StaticPlutoEventManager.fireEvent(ModUnload.class, new ModUnloadEvent());
        modArchive.clear();
    }

    private static void preLoadMods()
    {
        loadingPhase = ModLoadingPhase.PRELOADING;

        try
        {
            ModPreLoadEvent preLoadData = new ModPreLoadEvent();
            preLoadData.mods = modArchive;

            StaticPlutoEventManager.fireEvent(ModPreLoad.class, preLoadData);
        }
        catch (Exception e)
        {
            Logger.log("[Pluto Mod Loader] Problem encountered while preloading mods.");
            Logger.log("[Pluto Mod Loader] Mod loading stopped.");

            Logger.logException(e);

            cancelLoading();
        }
    }

    private static void loadMods()
    {
        loadingPhase = ModLoadingPhase.LOADING;

        try
        {
            ModLoadEvent loadData = new ModLoadEvent();

            StaticPlutoEventManager.fireEvent(ModLoad.class, loadData);
        }
        catch (Exception e)
        {
            Logger.log("[Pluto Mod Loader] Problem encountered while loading mods.");
            Logger.log("[Pluto Mod Loader] Mod loading stopped.");

            Logger.logException(e);

            cancelLoading();
        }
    }

    private static void postLoadMods()
    {
        loadingPhase = ModLoadingPhase.POSTLOADING;

        try
        {
            ModPostLoadEvent postLoadData = new ModPostLoadEvent();

            StaticPlutoEventManager.fireEvent(ModPostLoad.class, postLoadData);
        }
        catch (Exception e)
        {
            Logger.log("[Pluto Mod Loader] Problem encountered while loading mods.");
            Logger.log("[Pluto Mod Loader] Mod loading stopped.");

            Logger.logException(e);

            cancelLoading();
        }
    }

    private static void complete()
    {
        loadingPhase = ModLoadingPhase.FINISHING;
    }

    private static void cancelLoading()
    {
        loadingPhase = ModLoadingPhase.CANCELED;
    }
}
