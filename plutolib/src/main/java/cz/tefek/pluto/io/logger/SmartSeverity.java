package cz.tefek.pluto.io.logger;

/**
 * A more visual way to denote what's actually happening.
 * 
 * @author 493msi
 *
 * @since pre-alpha
 */
public enum SmartSeverity implements ISeverity
{
    ADDED("[+] ", false),
    REMOVED("[-] ", false),

    ZERO("[0] ", false),

    INFO("[i] ", false),
    WARNING("[!] ", true),
    ERROR("[X] ", true),

    AUDIO("[i] [♪] ", false),
    AUDIO_PLUS("[+] [♪] ", false),
    AUDIO_MINUS("[-] [♪] ", false),
    AUDIO_WARNING("[!] [♪] ", true),
    AUDIO_ERROR("[X] [♪] ", true),

    MODULE("[i] [M] ", false),
    MODULE_PLUS("[+] [M] ", false),
    MODULE_MINUS("[-] [M] ", false),
    MODULE_WARNING("[!] [M] ", true),
    MODULE_ERROR("[X] [M] ", true),

    EVENT("[i] [E] ", false),
    EVENT_PLUS("[+] [E] ", false),
    EVENT_MINUS("[-] [E] ", false),
    EVENT_WARNING("[!] [E] ", true),
    EVENT_ERROR("[X] [E] ", true);

    private final String displayName;
    private final boolean usesStdErr;

    SmartSeverity(String name, boolean usesStdErr)
    {
        this.displayName = name;
        this.usesStdErr = usesStdErr;
    }

    @Override
    public String getDisplayName()
    {
        return this.displayName;
    }

    @Override
    public boolean isStdErr()
    {
        return this.usesStdErr;
    }

}
