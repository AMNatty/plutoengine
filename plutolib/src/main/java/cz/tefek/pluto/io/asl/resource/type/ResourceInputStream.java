package cz.tefek.pluto.io.asl.resource.type;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import cz.tefek.pluto.io.asl.resource.Resource;
import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;

/**
 * {@link ResourceAddress} in, {@link InputStream} out.
 * 
 * @author 493msi
 */
public class ResourceInputStream extends Resource<InputStream>
{
    public ResourceInputStream(ResourceAddress raddress)
    {
        super(raddress, false);
    }

    @Override
    protected InputStream loadFromFile()
    {
        try
        {
            return Files.newInputStream(this.address.toNIOPath());
        }
        catch (IOException e)
        {
            Logger.log(SmartSeverity.ERROR, "Failed to open " + this.address + "!");
            Logger.log(e);
        }

        return null;
    }

}
