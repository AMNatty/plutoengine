package org.plutoengine.mod;

import org.plutoengine.address.VirtualAddress;
import org.plutoengine.address.VirtualAddressParseException;
import org.plutoengine.component.ComponentManager;
import org.plutoengine.component.ComponentToken;
import org.plutoengine.component.PlutoLocalComponent;
import org.plutoengine.logger.Logger;
import org.plutoengine.logger.SmartSeverity;

import java.util.*;

/**
 *
 *
 * @author 493msi
 * @since 20.2.0.0-alpha.3
 */
public final class ModLoader extends PlutoLocalComponent
{
    public static final ComponentToken<ModLoader> TOKEN = ComponentToken.create(ModLoader::new);

    private EnumModLoadingPhase loadingPhase;
    private final Map<VirtualAddress, Mod> modNameLookup;
    private final Map<Class<?>, Mod> modLookup;
    private final Deque<Mod> loadedModStack;
    private final Set<Mod> loadList;
    private final Map<Mod, IModEntryPoint> entryPoints;

    /**
     *
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    private ModLoader()
    {
        this.loadingPhase = EnumModLoadingPhase.INITIAL;
        this.modNameLookup = new HashMap<>();
        this.modLookup = new HashMap<>();
        this.loadedModStack = new LinkedList<>();
        this.loadList = new LinkedHashSet<>();
        this.entryPoints = new HashMap<>();
    }

    /**
     * Denotes whether this component should be unique.
     * Unique components can only exist once per instance
     * in a given {@link ComponentManager}.
     *
     * @return whether this component should be unique
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    @Override
    public boolean isUnique()
    {
        return true;
    }

    /**
     *
     * <p>
     * <b>Note:</b> This method can only be called before the initialization process begins.
     * </p>
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    public void registerMod(Class<?> modClass)
    {
        if (loadingPhase != EnumModLoadingPhase.INITIAL && loadingPhase != EnumModLoadingPhase.CLASSLOADING_EXTERNAL)
        {
            Logger.logf(SmartSeverity.MODULE_ERROR, "Cannot register a mod during loading phase %s!%n", loadingPhase.name());
            return;
        }

        if (modLookup.containsKey(modClass))
        {
            Logger.logf(SmartSeverity.MODULE_CHECK, "Mod class '%s' is already registered, skipping it.%n", modClass.getCanonicalName());
            return;
        }

        var modInterface = modClass.getDeclaredAnnotation(ModEntry.class);

        if (modInterface == null)
        {
            Logger.logf(SmartSeverity.MODULE_WARNING, "Cannot create a mod from the class '%s', it is not annotated with @%s%n".formatted(modClass.getCanonicalName(), ModEntry.class.getName()));
            return;
        }

        VirtualAddress modID;

        try
        {
            modID = VirtualAddress.parse(modInterface.modID(), false);
        }
        catch (VirtualAddressParseException e)
        {
            Logger.logf(SmartSeverity.MODULE_WARNING, "The ID of mod '%s' is empty or invalid, it will not be loaded.%n", modClass);
            return;
        }

        var mod = Mod.from(modID, modInterface.dependencies(), modInterface.version(), modClass);


        var dependencies = mod.getDependencies();

        for (var dependency : dependencies)
        {
            if (modLookup.containsKey(dependency))
                continue;

            Logger.logf(SmartSeverity.MODULE_PLUS, "Adding '%s' as a dependency of '%s'.%n", dependency.getCanonicalName(), mod.getID());

            this.registerMod(dependency);
        }

        this.modNameLookup.put(modID, mod);
        this.modLookup.put(modClass, mod);
        this.loadList.add(mod);
    }

    /**
     * Returns all loaded mods in their load order order.
     *
     * @return A collection of all loaded mods
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    public Collection<Mod> getAllMods()
    {
        return Collections.unmodifiableCollection(this.loadedModStack);
    }

    /**
     * Attempts to find a {@link Mod} with the corresponding mod ID and returns it,
     * otherwise returns an empty {@link Optional}.
     *
     * @param modID A mod ID string
     *
     * @return A {@link Mod} with the corresponding ID; or an empty optional
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    public Optional<Mod> getModByID(VirtualAddress modID)
    {
        return Optional.ofNullable(this.modNameLookup.get(modID));
    }

    /**
     *
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    public void load()
    {
        Logger.log(SmartSeverity.MODULE, "Number of pre-added mod/s: " + this.loadList.size());

        this.loadingPhase = EnumModLoadingPhase.SCANNING_EXTERNAL;

        // TODO: Load external mods

        this.loadingPhase = EnumModLoadingPhase.CLASSLOADING_EXTERNAL;

        // TODO: Classload external mods

        this.loadingPhase = EnumModLoadingPhase.LOADING;

        Logger.log(SmartSeverity.MODULE, "Mod initialization started.");

        try
        {
            int modCount = this.loadList.size();
            int i = 0;

            for (var mod : this.loadList)
            {
                i++;

                Logger.logf(SmartSeverity.MODULE, "[%d / %d] Initializing '%s'...%n", i, modCount, mod.getID());

                var modClass = mod.getMainClass();

                if (IModEntryPoint.class.isAssignableFrom(modClass))
                {
                    var entryPoint = modClass.asSubclass(IModEntryPoint.class);
                    Logger.logf(SmartSeverity.MODULE, "[%d / %d] Mod '%s' has an entry point, constructing...%n", i, modCount, mod.getID());

                    try
                    {
                        var constructor = entryPoint.getConstructor();
                        IModEntryPoint entryPointInstance = constructor.newInstance();
                        Logger.logf(SmartSeverity.MODULE, "[%d / %d] Running onLoad for mod '%s'...%n", i, modCount, mod.getID());
                        entryPointInstance.onLoad(mod);
                        this.entryPoints.put(mod, entryPointInstance);
                    }
                    catch (NoSuchMethodException e)
                    {
                        Logger.logf(SmartSeverity.MODULE_ERROR, "[%d / %d] Error: Mod '%s' has an entry point but does not have an accessible default constructor!" +
                                                                " It will not be initialized...%n", i, modCount, mod.getID());
                    }
                }

                this.loadedModStack.addLast(mod);
            }

            this.loadingPhase = EnumModLoadingPhase.DONE;
        }
        catch (Exception e)
        {
            Logger.log(SmartSeverity.MODULE_ERROR, "Caught the following exception while initializing mods:");
            Logger.log(e);

            Logger.log(SmartSeverity.MODULE_ERROR, "Mod initialization was canceled due to an error.");
        }

        if (this.loadingPhase == EnumModLoadingPhase.DONE)
        {
            Logger.log(SmartSeverity.MODULE_CHECK, "Mod initialization finished.");
        }
        else
        {
            Logger.log(SmartSeverity.MODULE_ERROR, "Mod initialization incomplete.");
        }
    }

    /**
     *
     *
     * @author 493msi
     * @since 20.2.0.0-alpha.3
     */
    public void unload()
    {
        this.loadingPhase = EnumModLoadingPhase.UNLOADING;
        Logger.log(SmartSeverity.MODULE_MINUS, "Unloading all mods.");

        int modCount = this.loadedModStack.size();
        int i = 0;

        while (!this.loadedModStack.isEmpty())
        {
            i++;

            var mod = this.loadedModStack.removeLast();

            Logger.logf(SmartSeverity.MODULE, "[%d / %d] Deinitializing '%s'...%n", i, modCount, mod.getID());

            var entryPointInstance = this.entryPoints.get(mod);

            if (entryPointInstance != null)
            {
                Logger.logf(SmartSeverity.MODULE, "[%d / %d] Running onUnload for mod '%s'...%n", i, modCount, mod.getID());
                entryPointInstance.onUnload();
            }
        }

        this.modNameLookup.clear();
        this.loadList.clear();
        this.loadedModStack.clear();
        this.modLookup.clear();
        this.entryPoints.clear();
    }
}
