package cz.tefek.pluto.lmdb;

/**
 * @deprecated Usage discouraged until tested well enough.
 * */
@Deprecated
public class LMDBSchema<K extends LMDBKey, E extends ILMDBValueRecipe>
{
    private String name;
    private Class<E> valueType;

    public LMDBSchema(String name, Class<E> valueType)
    {
        this.name = name;
        this.valueType = valueType;
    }

    public String getName()
    {
        return this.name;
    }

    public Class<E> getValueRecipe()
    {
        return this.valueType;
    }
}
