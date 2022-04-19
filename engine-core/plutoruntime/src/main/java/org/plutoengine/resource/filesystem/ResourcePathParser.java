package org.plutoengine.resource.filesystem;

import org.plutoengine.address.VirtualAddress;
import org.plutoengine.address.VirtualAddressParseException;
import org.plutoengine.address.VirtualAddressParser;

import java.net.URI;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ResourcePathParser
{
    private enum State
    {
        RS_BEGIN,
        RS_MOD_ID,
        RS_FILE_SYSTEM,
        RS_ADDRESS,
        RS_EXTENSION
    }
    
    public static final int TOKEN_FILE_EXTENSION_SEPARATOR = '#';
    public static final int TOKEN_FILE_SYSTEM_SEPARATOR = '+';
    public static final int TOKEN_ABSOLUTE_SEPARATOR = '$';
    public static final int TOKEN_RELATIVE_SEPARATOR = '!';

    private VirtualAddress modID;
    private VirtualAddress containerID;
    private VirtualAddress pathAddress;
    private String extension;

    private VirtualAddressParser modIDParser;
    private VirtualAddressParser containerIDParser;
    private VirtualAddressParser pathAddressParser;
    private StringBuilder extensionBuilder;

    private boolean isRelative;
    private State state;

    private ResourcePathParser()
    {
        this.state = State.RS_BEGIN;
    }

    private void accept(int codepoint)
    {
        switch (this.state)
        {
            case RS_BEGIN -> {
                if (codepoint == TOKEN_RELATIVE_SEPARATOR)
                {
                    this.isRelative = true;
                    this.pathAddressParser = VirtualAddress.createParser(true);
                    this.state = State.RS_ADDRESS;
                }
                else
                {
                    this.modIDParser = VirtualAddress.createParser(false);
                    this.modIDParser.accept(codepoint);
                    this.state = State.RS_MOD_ID;
                }
            }

            case RS_MOD_ID -> {
                switch (codepoint)
                {
                    case TOKEN_RELATIVE_SEPARATOR, TOKEN_ABSOLUTE_SEPARATOR, TOKEN_FILE_SYSTEM_SEPARATOR -> {
                        this.modID = this.modIDParser.build();

                        switch (codepoint)
                        {
                            case TOKEN_FILE_SYSTEM_SEPARATOR -> {
                                this.containerIDParser = VirtualAddress.createParser(false);
                                this.state = State.RS_FILE_SYSTEM;
                            }

                            case TOKEN_ABSOLUTE_SEPARATOR -> {
                                this.pathAddressParser = VirtualAddress.createParser(false);
                                this.state = State.RS_ADDRESS;
                            }
                        }


                        this.isRelative = false;
                    }

                    default -> this.modIDParser.accept(codepoint);
                }
            }

            case RS_FILE_SYSTEM -> {
                if (codepoint == TOKEN_ABSOLUTE_SEPARATOR)
                {
                    this.pathAddressParser = VirtualAddress.createParser(this.isRelative);
                    this.state = State.RS_ADDRESS;
                    this.containerID = this.containerIDParser.build();
                }
                else
                {
                    this.containerIDParser.accept(codepoint);
                }
            }

            case RS_ADDRESS -> {
                if (codepoint == TOKEN_FILE_EXTENSION_SEPARATOR)
                {
                    this.pathAddress = this.pathAddressParser.build();
                    this.extensionBuilder = new StringBuilder();
                    this.state = State.RS_EXTENSION;
                }
                else
                {
                    this.pathAddressParser.accept(codepoint);
                }
            }

            case RS_EXTENSION -> {
                if (!Character.isLetterOrDigit(codepoint))
                    throw new ResourcePathParseException("Unexpected character in the extension!");

                this.extensionBuilder.appendCodePoint(codepoint);
            }
        }
    }

    private UnresolvedResourcePath build()
    {
        switch (this.state)
        {
            case RS_ADDRESS -> this.pathAddress = this.pathAddressParser.build();
            case RS_EXTENSION -> this.extension = this.extensionBuilder.toString();
            default -> throw new ResourcePathParseException("Unexpected end of ResourcePath!");
        }

        if (this.extension != null && (this.extension.isEmpty() || this.extension.length() > ResourcePath.MAX_EXTENSION_CHARACTERS))
            throw new ResourcePathParseException("The ResourcePath extension must not be blank (when set) or longer than " + ResourcePath.MAX_EXTENSION_CHARACTERS + "!");
        
        return new UnresolvedResourcePath(this.modID, this.containerID, this.isRelative, this.pathAddress, this.extension);
    }

    
    /**
     * modID+resource.root$absolute.path.to.item[#extension]
     * !relative.path.to.item[#extension]
     * !~.path.to.item[#extension]
     *
     * modID                   unique mod identifier
     * assetContainerID        asset container (optional, default asset pack if unspecified)
     * $                       absolute path
     * !                       relative path
     *
     * .                       path separator
     * ~                       go up one directory
     *
     * #extension              extension separator (optional)
     *
     * @author 493msi
     *
     */
    
    private static UnresolvedResourcePath parse(IntStream it)
    {
        var parser = new ResourcePathParser();
        IntConsumer parserConsumer = parser::accept;
        it.forEachOrdered(parserConsumer);
        return parser.build();
    }

    //  +---------------------------------------------+------------+---------+----------------------------+--------------------------+--------------------------+----------+
    //  |                    input                    |   scheme   | opaque  |             SSP            |        authority         |         path             | fragment |
    //  +---------------------------------------------+------------+---------+----------------------------+--------------------------+--------------------------+----------+
    //  | pluto+asl:mod.id$path.to.resource#ext       | pluto+asl  | true    | mod.id$path.to.resource    |                          |                          | ext      |
    //  | pluto+asl://mod.id$path.to.resource#ext     | pluto+asl  | false   | //mod.id$path.to.resource  | mod.id$path.to.resource  |                          | ext      |
    //  +---------------------------------------------+------------+---------+----------------------------+--------------------------+--------------------------+----------+

    /**
     *
     * @param uri The input URI
     * @return An {@link UnresolvedResourcePath}
     */
    public static UnresolvedResourcePath parse(URI uri)
    {
        if (uri == null)
            return null;

        if (!ResourceManager.URI_SCHEME.equals(uri.getScheme()))
            throw new ResourcePathParseException("Invalid URI scheme!");

        var normalizedAddress = uri.isOpaque() ? uri.getSchemeSpecificPart() : uri.getAuthority();

        if (normalizedAddress == null)
            throw new ResourcePathParseException("Could not find a valid resource path in the supplied URI!");

        var codepoints = normalizedAddress.codePoints();

        var extension = uri.getFragment();

        if (extension != null)
        {
            var extensionCodepoints = IntStream.concat(IntStream.of(TOKEN_FILE_EXTENSION_SEPARATOR), extension.codePoints());
            codepoints = IntStream.concat(codepoints, extensionCodepoints);
        }

        try
        {
            return parse(codepoints);
        }
        catch (VirtualAddressParseException | ResourcePathParseException e)
        {
            throw new ResourcePathParseException(e, "Failed to parse %s: %s".formatted(ResourcePath.class.getSimpleName(), uri));
        }
    }

    public static UnresolvedResourcePath parse(String pathStr)
    {
        if (pathStr == null)
            return null;

        try
        {
            return parse(pathStr.codePoints());
        }
            catch (VirtualAddressParseException e)
        {
            throw new ResourcePathParseException(e, "Failed to parse %s: %s".formatted(ResourcePath.class.getSimpleName(), pathStr));
        }
    }
}
