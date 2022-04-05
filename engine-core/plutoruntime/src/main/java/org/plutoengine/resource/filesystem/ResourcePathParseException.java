package org.plutoengine.resource.filesystem;

public class ResourcePathParseException extends RuntimeException
{
    public ResourcePathParseException(String message)
    {
        super(message);
    }

    public ResourcePathParseException(Throwable cause, String message)
    {
        super(message, cause);
    }
}
