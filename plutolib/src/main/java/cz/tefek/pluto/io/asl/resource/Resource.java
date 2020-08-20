package cz.tefek.pluto.io.asl.resource;

/**
 * Allows loading a resource from the file system location defined by the
 * supplied {@link ResourceAddress}.
 * 
 * For example <tt>Resource&lt;String&gt;</tt> means you have a
 * <tt>Resource</tt> that will output a String when loaded.
 *
 * @author 493msi
 *
 * @param R The type of the loaded <tt>Resource</tt>.
 */
public abstract class Resource<R>
{
    protected ResourceAddress address;
    protected boolean virtual;

    protected R value;

    public Resource(ResourceAddress raddress, boolean virtual)
    {
        this.address = raddress;
        this.virtual = virtual;
    }

    public R load()
    {
        return this.virtual ? this.value : this.loadFromFile();
    }

    protected abstract R loadFromFile();
}
