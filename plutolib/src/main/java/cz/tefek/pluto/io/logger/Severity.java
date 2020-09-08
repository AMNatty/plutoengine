package cz.tefek.pluto.io.logger;

/**
 * Message severity.
 *
 * @author 493msi
 *
 * @deprecated Use {@link SmartSeverity} instead.
 */
@Deprecated
public enum Severity implements ISeverity
{
    INFO("[INFO] ", false),
    WARNING("[WARNING] ", true),
    ERROR("[ERROR] ", true),
    EXCEPTION("[EXCEPTION] ", true),
    NONE("", false);

    private String displayName;
    private boolean usesStdErr;

    Severity(String name, boolean usesStdErr)
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
