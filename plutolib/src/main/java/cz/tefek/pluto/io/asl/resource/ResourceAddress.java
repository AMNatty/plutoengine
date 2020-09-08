package cz.tefek.pluto.io.asl.resource;

import javax.annotation.Nullable;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cz.tefek.pluto.io.logger.Logger;
import cz.tefek.pluto.io.logger.SmartSeverity;
import cz.tefek.pluto.modloader.ModLoaderCore;

/**
 * Resource address is a universal key for all resource and file loading. You
 * just need a {@link ResourceSubscriber} (which holds the root folder location)
 * and a {@link String} containing the address. The address itself works like a
 * Java package. For example <i>"sample.textures.test"</i> formats as
 * <code>[root_folder]/sample/textures/test</code> when converted using
 * <code>toPath()</code>. To define a file extension for your address, use the
 * <code>fileExtension(String)</code> method. To remove the file extension use
 * <code>fileExtension(null)</code>.
 *
 * @author 493msi
 *
 * @see ResourceSubscriber
 */
public class ResourceAddress
{
    public static final int LIMIT = 32;
    public static final int MAX_BRANCH_STRING_LENGTH = 32;

    public static final String BRANCH_STRING_PATTERN = "[a-zA-Z0-9_]+";
    public static final String FILE_EXTENSION_PATTERN = "[a-zA-Z0-9_]+";

    public static final String RESOURCE_ADDRESS_STRING_PATTERN = "^(" + ModLoaderCore.MOD_ID_STRING_PATTERN + ")\\$((?:" + BRANCH_STRING_PATTERN + "\\.)*?" + BRANCH_STRING_PATTERN + "?)(?:#([a-zA-Z0-9_]+))?$";
    public static final Pattern RESOURCE_ADDRESS_PATTERN = Pattern.compile(RESOURCE_ADDRESS_STRING_PATTERN, Pattern.CASE_INSENSITIVE);

    protected final List<String> subAddress = new ArrayList<>(LIMIT);
    protected ResourceSubscriber resSubscriber;

    protected String fileExtension = null;

    /**
     * Copy constructor for internal use.
     */
    protected ResourceAddress(ResourceSubscriber subscriber, List<String> subAddr, String fileExtension)
    {
        this.resSubscriber = subscriber;

        this.subAddress.addAll(subAddr);

        this.fileExtension(fileExtension);
    }

    public ResourceAddress(ResourceSubscriber sub, String address)
    {
        if (sub == null)
        {
            throw new IllegalArgumentException("Empty resource subscriber is not allowed.");
        }

        this.resSubscriber = sub;

        var parts = address.split("#");

        if (parts.length == 2)
        {
            address = parts[0];
            this.fileExtension(parts[1]);
        }
        else if (parts.length > 2)
        {
            throw new IllegalArgumentException("Illegal ResourceAddress component count!");
        }

        if (address == null)
        {
            throw new IllegalArgumentException("Empty address is not allowed.");
        }

        String[] as = address.split("\\.");

        if (as.length == 0)
        {
            throw new IllegalArgumentException("Zero length address is not allowed.");
        }

        if (as.length >= LIMIT)
        {
            throw new IllegalArgumentException("Address can't branch deeper more than " + (LIMIT - 1) + " times.");
        }

        if (as.length == 1)
        {
            Logger.log(SmartSeverity.WARNING, "Please do not use tier 1 addresses, so it doesn't conflict with core assets.");
        }

        for (String branch : as)
        {
            if (branch.length() < 1 || branch.length() > MAX_BRANCH_STRING_LENGTH)
            {
                throw new IllegalArgumentException("Length of branch must be higher than 0 and lower than 33, this is not an essay.");
            }

            if (!branch.matches("^[a-zA-Z0-9_]+$"))
            {
                throw new IllegalArgumentException("Wrong address branch format: " + branch);
            }

            this.subAddress.add(branch);
        }
    }

    public static ResourceAddress parse(String strVal)
    {
        if (strVal == null)
        {
            return null;
        }

        if (!strVal.matches(RESOURCE_ADDRESS_STRING_PATTERN))
        {
            throw new IllegalArgumentException("Malformed resource address: " + strVal);
        }

        var addressComponents = strVal.split("[$#]");

        if (addressComponents.length < 2 || addressComponents.length > 3)
        {
            throw new IllegalArgumentException("Illegal ResourceAddress component count!");
        }

        var mod = ModLoaderCore.getModByID(addressComponents[0]);

        if (mod == null)
        {
            throw new IllegalArgumentException(String.format("Mod with ID %s not found!", addressComponents[0]));
        }

        var raddress = new ResourceAddress(mod.getDefaultResourceSubscriber(), addressComponents[1]);

        if (addressComponents.length == 3)
        {
            raddress.fileExtension(addressComponents[2]);
        }

        return raddress;
    }

    public ResourceAddress fileExtension(@Nullable String ext)
    {
        if (ext == null || "".equals(ext))
        {
            this.fileExtension = null;
            return this;
        }

        if (!ext.matches(FILE_EXTENSION_PATTERN))
        {
            throw new IllegalArgumentException("@ResourceAddress.fileExtension: Wrong file extension format. Only english alphabet, numbers and underscore are permitted.");
        }

        this.fileExtension = ext;
        return this;
    }

    public boolean hasFileExtension()
    {
        if (this.fileExtension == null)
        {
            return false;
        }

        return !this.fileExtension.isBlank();
    }

    public String getFileExtension()
    {
        return this.fileExtension;
    }

    public ResourceAddress branch(String branch)
    {
        if (branch == null)
        {
            throw new NullPointerException("@ResourceAddress.branch: INPUT = NULL!");
        }

        if (branch.length() < 1 || branch.length() > MAX_BRANCH_STRING_LENGTH)
        {
            throw new IllegalArgumentException("@ResourceAddress.branch: Length of branch must be higher than 0 and lower than " + (MAX_BRANCH_STRING_LENGTH + 1) + ".");
        }

        if (!branch.matches(BRANCH_STRING_PATTERN))
        {
            throw new IllegalArgumentException("@ResourceAddress.branch: Wrong branch format. Only english alphabet, numbers and underscore are permitted.");
        }

        if (this.subAddress.size() >= LIMIT)
        {
            throw new IllegalArgumentException("@ResourceAddress.branch: Address can't branch deeper more than " + (LIMIT - 1) + " times.");
        }

        this.subAddress.add(branch);

        return this;
    }

    @Override
    public String toString()
    {
        return this.resSubscriber.getMod().getModID() + "$" + this.subAddressToString();
    }

    public ResourceAddress copy()
    {
        return new ResourceAddress(this.resSubscriber, this.subAddress, this.fileExtension);
    }

    public String toPath()
    {
        StringBuilder sbPath = new StringBuilder(this.resSubscriber.getRootPath());

        for (String branch : this.subAddress)
        {
            if (branch == null)
            {
                break;
            }

            sbPath.append("/");
            sbPath.append(branch);
        }

        if (this.hasFileExtension())
        {
            sbPath.append(".").append(this.fileExtension);
        }

        return sbPath.toString();
    }

    public String subAddressToString()
    {
        StringBuilder sbPath = new StringBuilder();

        boolean firstLoop = true;

        for (String branch : this.subAddress)
        {
            if (branch == null)
            {
                break;
            }

            if (!firstLoop)
            {
                sbPath.append(".");
            }
            else
            {
                firstLoop = false;
            }

            sbPath.append(branch);
        }

        if (this.hasFileExtension())
        {
            sbPath.append("#");
            sbPath.append(this.fileExtension);
        }

        return sbPath.toString();
    }

    public Path toNIOPath()
    {
        var pathBuilder = new StringBuilder(this.resSubscriber.getRootPath());
        final var separator = FileSystems.getDefault().getSeparator();
        pathBuilder.append(separator);
        pathBuilder.append(String.join(separator, this.subAddress));

        if (this.hasFileExtension())
        {
            pathBuilder.append('.');
            pathBuilder.append(this.fileExtension);
        }

        return Path.of(pathBuilder.toString());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.fileExtension == null) ? 0 : this.fileExtension.hashCode());
        result = prime * result + this.resSubscriber.hashCode();
        result = prime * result + this.subAddress.hashCode();
        return result;
    }
}
