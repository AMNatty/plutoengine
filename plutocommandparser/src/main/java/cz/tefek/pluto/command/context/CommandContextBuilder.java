package cz.tefek.pluto.command.context;

import cz.tefek.pluto.command.CommandBase;

public class CommandContextBuilder
{
    private final CommandContext ctx;

    public CommandContextBuilder()
    {
        this.ctx = new CommandContext();
    }

    public CommandContextBuilder prefix(String prefix)
    {
        this.ctx.usedPrefix = prefix;

        return this;
    }

    public CommandContextBuilder alias(String alias)
    {
        this.ctx.usedAlias = alias;

        return this;
    }

    public CommandContextBuilder command(CommandBase command)
    {
        this.ctx.command = command;

        return this;
    }

    public CommandContext resolved()
    {
        this.ctx.resolved = true;

        return this.ctx;
    }

    public CommandContext unresolved(EnumCommandParseFailure cause)
    {
        this.ctx.resolved = false;

        return this.ctx;
    }

    public static class CommandContext
    {
        private boolean resolved;
        private EnumCommandParseFailure failureCause;

        private String usedPrefix;
        private String usedAlias;
        private CommandBase command;

        private CommandContext()
        {

        }

        public String getUsedPrefix()
        {
            return this.usedPrefix;
        }

        public String getUsedAlias()
        {
            return this.usedAlias;
        }

        public CommandBase getCommand()
        {
            return this.command;
        }

        public boolean isResolved()
        {
            return this.resolved;
        }

        public EnumCommandParseFailure getFailureCause()
        {
            return this.failureCause;
        }
    }

    public enum EnumCommandParseFailure
    {
        UNRESOLVED_PREFIX,
        UNRESOLVED_COMMAND_NAME,
        UNRESOLVED_PARAMETERS,
        UNRESOLVED_UNEXPECTED_STATE
    }
}
