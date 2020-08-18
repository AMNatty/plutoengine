package cz.tefek.io.pluto.pp;

/**
 * Thrown when a provided package definition is for some reason not valid.
 *
 * @author 493msi
 */
public class InvalidPlutoPackageException extends Exception
{
    private static final long serialVersionUID = 7853852981742059946L;

    public InvalidPlutoPackageException(String message, Throwable e)
    {
        super(message, e);
    }
}
