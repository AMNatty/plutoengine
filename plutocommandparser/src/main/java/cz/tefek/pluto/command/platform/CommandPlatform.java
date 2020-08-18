package cz.tefek.pluto.command.platform;

import cz.tefek.pluto.command.context.CommandContextBuilder.EnumCommandParseFailure;

public abstract class CommandPlatform
{
    public abstract String getID();

    public abstract String getName();

    public boolean shouldWarnOn(EnumCommandParseFailure failure)
    {
        switch (failure)
        {
            case UNRESOLVED_PREFIX:
                return false;

            case UNRESOLVED_COMMAND_NAME:
                return false;

            case UNRESOLVED_UNEXPECTED_STATE:
                return false;

            default:
                return true;
        }
    }

    public abstract int getMessageLimit();
}
