package cz.tefek.pluto.io.asl.textio;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.logger.Logger;

/**
 * A simple text file reader. Apart from generic methods of loading, you can use
 * a {@link ResourceAddress}. For writing use {@link TextOut}.
 *
 * @author 493msi
 */
public class TextIn
{
    public static String load(URL url)
    {
        try
        {
            load(url.toURI());
        }
        catch (URISyntaxException e)
        {
            Logger.log(e);
        }

        return null;
    }

    public static String load(URI uri)
    {
        try
        {
            return Files.readString(Paths.get(uri));
        }
        catch (Exception e)
        {
            Logger.log(e);
        }

        return null;
    }

    public static String load(Path path)
    {
        try
        {
            return Files.readString(path);
        }
        catch (Exception e)
        {
            Logger.log(e);
        }

        return null;
    }

    public static String loadInternal(String filename)
    {
        return load(TextIn.class.getResource("/" + filename));
    }

    public static String loadExternal(String filename)
    {
        return load(new File(filename).toURI());
    }

    public static String fromAddress(ResourceAddress address)
    {
        return load(address.toNIOPath());
    }
}
