package cz.tefek.pluto.command;

import cz.tefek.pluto.annotation.ConstantExpression;

public interface ICommand
{
    @ConstantExpression
    String name();

    @ConstantExpression
    String[] aliases();

    @ConstantExpression
    String description();

    @ConstantExpression
    Class<?> commandClass();
}
