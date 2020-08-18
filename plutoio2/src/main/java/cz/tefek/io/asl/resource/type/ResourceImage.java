package cz.tefek.io.asl.resource.type;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cz.tefek.io.asl.resource.Resource;
import cz.tefek.io.asl.resource.ResourceAddress;
import cz.tefek.io.asl.resource.ResourceHelper;
import cz.tefek.io.pluto.debug.Logger;
import cz.tefek.io.pluto.debug.Severity;

/**
 * {@link ResourceAddress} in, {@link BufferedImage} out.
 *
 * @author 493msi
 */
public class ResourceImage extends Resource<BufferedImage>
{
    public ResourceImage(ResourceAddress raddress, boolean virtual)
    {
        super(raddress, virtual);
    }

    public ResourceImage(ResourceAddress raddress)
    {
        super(raddress, false);
    }

    @Override
    public BufferedImage loadFromFile()
    {
        try
        {
            return ImageIO.read(new File(this.address.toPath()));
        }
        catch (IOException e)
        {
            Logger.log(Severity.ERROR, "Could not load BufferedImage: " + this.address.toString() + ", will load placeholder.");
            Logger.logException(e);

            try
            {
                return ImageIO.read(new File(ResourceHelper.GLOBAL_ROOT + "data/assets/err/missingTex.png"));
            }
            catch (IOException e1)
            {
                Logger.log(Severity.ERROR, "Placeholder BufferedImage not found: " + ResourceHelper.GLOBAL_ROOT + "data/assets/err/missingTex.png");
                Logger.log("This is not good! :C");

                Logger.logException(e1);
            }

            return null;
        }
    }
}
