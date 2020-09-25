package cz.tefek.pluto;

/**
 * Constants shared by all Pluto libraries.
 *
 * @since pre-alpha
 *
 * @author 493msi
 */
public class Pluto
{
    public static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("cz.tefek.pluto.debug"));

    // TODO: Automate setting the version numbers with gradle

    /**
     * The year version number, changes with breaking API changes.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_YEAR = 20;

    /**
     * The major version number, changes with breaking API changes.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_MAJOR = 2;

    /**
     * The minor version number, changes with backwards-compatible API changes.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_MINOR = 0;

    /**
     * The patch number, changes with backwards-compatible fixes and patches.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_PATCH = 0;

    /**
     * Denotes whether this build is a pre-release build.
     *
     * @since pre-alpha
     * */
    public static final boolean PRERELEASE = true;

    /**
     * The name of this pre-release, e.g. alpha, beta, RC and similar.
     *
     * @since pre-alpha
     * */
    public static final String PRERELEASE_NAME = "alpha";

    /**
     * The pre-release patch number, incremented by 1 with *any* pre-release update.
     *
     * @since pre-alpha
     * */
    public static final int PRERELEASE_PATCH = 3;

    /**
     * The combined version string.
     *
     * @since pre-alpha
     * */
    public static final String VERSION = VERSION_YEAR + "." + VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH + (PRERELEASE ? "-" + PRERELEASE_NAME + "." + PRERELEASE_PATCH : "");
}
