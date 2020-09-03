package cz.tefek.pluto.io.asl.resource;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Helper functions for {@link ResourceAddress}es.
 *
 * @author 493msi
 */
public class ResourceHelper
{
    public static final String GLOBAL_ROOT = "";
    public static final String DEFAULT_RESOURCE_ROOT = GLOBAL_ROOT + "data";

    /**
     * Retrieves the size of the file denoted by {@link ResourceAddress}
     * 
     * @param addr The input {@link ResourceAddress}
     * 
     * @throws IOException On I/O errors.
     * 
     * @return the file size
     * @since 0.3
     */
    public static long fileSize(ResourceAddress addr) throws IOException
    {
        return Files.size(addr.toNIOPath());
    }
}
