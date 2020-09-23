package cz.tefek.pluto.command.resolver.command;

import cz.tefek.pluto.command.CommandBase;
import cz.tefek.pluto.command.invoke.InvokeHandler;

public final class TestCommand extends CommandBase
{
    @Override
    public String name()
    {
        return "test";
    }

    @Override
    public String[] aliases()
    {
        return new String[0];
    }

    @Override
    public String description()
    {
        return "The test command - prints Hello World to stdout.";
    }

    @InvokeHandler
    public void invoke()
    {
        System.out.println("Hello World!");
    }
}
