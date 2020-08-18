package cz.tefek.pluto.command.resolver;

public class ResolveException extends AbstractResolveException
{
    /**
     * 
     */
    private static final long serialVersionUID = -3098373878754649161L;

    private EnumResolveFailure failure;

    public ResolveException(EnumResolveFailure failure)
    {
        this.failure = failure;
    }

    public EnumResolveFailure getResolveFailure()
    {
        return this.failure;
    }

}