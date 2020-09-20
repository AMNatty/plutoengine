package cz.tefek.pluto.command;

public abstract class CommandBase
{
    public abstract String name();

    public abstract String[] aliases();

    public abstract String description();

    public abstract Class<?> commandClass();

    @Override
    public final int hashCode()
    {
        return this.name().hashCode();
    }
}
