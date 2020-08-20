package cz.tefek.pluto.io.asl.resource.type;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.tefek.pluto.io.asl.resource.Resource;
import cz.tefek.pluto.io.asl.resource.ResourceAddress;
import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.Severity;

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
            return new FileInputStream(this.address.toPath());
        }
        catch (IOException e)
        {
            Logger.log(Severity.EXCEPTION, "Failed to open " + this.address + "!");
            Logger.log(Severity.EXCEPTION, e);
        }

        return null;
    }

}
