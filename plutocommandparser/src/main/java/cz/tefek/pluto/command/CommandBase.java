package cz.tefek.pluto.command;

import java.lang.reflect.Modifier;

import cz.tefek.pluto.command.invoke.InvokeHandler;

public abstract class CommandBase implements ICommand
{
    private final Class<? extends  CommandBase> clazz;

    public final Class<?> commandClass()
    {
        return this.clazz;
    }

    protected CommandBase()
    {
        this.clazz = this.getClass();

        if (!Modifier.isFinal(this.clazz.getModifiers()))
        {
            // Class must be final
            // Throwing an exception here is okay, since this is the developer's fault
            //
            // You can still create abstract wrappers for CommandBase, however implemented commands
            // must be final.
            throw new RuntimeException("Command classes MUST be final. Offender: " + this.clazz);
        }

        var methods = this.clazz.getMethods();

        for (var method : methods)
        {
            // Silently skip methods without annotations
            if (!method.isAnnotationPresent(InvokeHandler.class))
                continue;

            var modifiers = method.getModifiers();

            if (Modifier.isStatic(modifiers))
            {
                // Method must be non-static
                // Throwing an exception here is okay, since this is the developer's fault
                throw new RuntimeException("Invoke handlers MUST NOT be static. Offender: " + method);
            }

            if (!Modifier.isPublic(modifiers))
            {
                // Method must be public
                // Throwing an exception here is okay, since this is the developer's fault
                throw new RuntimeException("Invoke handlers MUST be public. Offender: " + method);
            }


        }
    }

    @Override
    public final int hashCode()
    {
        return this.name().hashCode();
    }
}
