package cz.tefek.pluto.command.resolver;

public abstract class AbstractResolveException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = -8934505669078637864L;

    public AbstractResolveException(Exception exception)
    {
        super(exception);
    }

    public AbstractResolveException(String what)
    {
        super(what);
    }

    protected AbstractResolveException()
    {

    }
}
