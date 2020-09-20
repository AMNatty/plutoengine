package cz.tefek.pluto.command.registry;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.tefek.pluto.command.CommandBase;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

public final class CommandRegistry
{
    private static CommandRegistry instance;

    private final Set<CommandBase> commands;
    private final Map<String, CommandBase> aliasTable;

    static
    {
        instance = new CommandRegistry();
    }

    private CommandRegistry()
    {
        this.aliasTable = new HashMap<>();
        this.commands = new HashSet<>();
    }

    private void registerAlias(String alias, CommandBase command)
    {
        if (this.aliasTable.containsKey(alias))
        {
            Logger.logf(SmartSeverity.ERROR, "Alias '%s' for command '%s' is already used, skipping.%n", alias, command.name());

            return;
        }

        this.aliasTable.put(alias, command);
    }

    public static void registerCommand(CommandBase command)
    {
        if (!instance.commands.add(command))
        {
            Logger.logf(SmartSeverity.ERROR, "Command '%s' is already registered, skipping.%n", command.name());
            return;
        }

        instance.registerAlias(command.name(), command);

        Arrays.stream(command.aliases()).forEach(alias -> instance.registerAlias(alias, command));
    }

    public static CommandBase getByAlias(String alias)
    {
        return instance.aliasTable.get(alias);
    }

    public static Set<CommandBase> getCommands()
    {
        return Collections.unmodifiableSet(instance.commands);
    }

    public static void clear()
    {
        instance = new CommandRegistry();
    }
}
