package cz.tefek.io.asl.resource;

import cz.tefek.io.modloader.Mod;

/**
 * Allows access to resources using {@link ResourceAddress}</tt>. Requires a
 * {@link Mod} instance to operate.
 *
 * @author 493msi
 */
public class ResourceSubscriber
{
    private Mod owner;
    private String rootFolder;

    public ResourceSubscriber(Mod mod)
    {
        this(mod, ResourceHelper.DEFAULT_RESOURCE_ROOT);
    }

    public ResourceSubscriber(Mod mod, String root)
    {
        if (mod == null)
        {
            throw new IllegalArgumentException("Mod can't be null!");
        }

        this.owner = mod;
        this.rootFolder = root;
    }

    public Mod getMod()
    {
        return this.owner;
    }

    public String getRootPath()
    {
        return this.rootFolder;
    }

    @Override
    public int hashCode()
    {
        return this.owner.getModID().hashCode();
    }
}
