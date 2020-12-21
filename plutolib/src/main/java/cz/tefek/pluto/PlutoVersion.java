package cz.tefek.pluto;

import javax.annotation.Nonnull;

import java.util.Objects;

import cz.tefek.pluto.annotation.ConstantExpression;

public final class PlutoVersion implements IVersion<PlutoVersion>
{
    private final int year;
    private final int major;
    private final int minor;
    private final int patch;

    private final boolean prerelease;
    private final String prereleaseName;
    private final int prereleaseNumber;

    public PlutoVersion(int year, int major, int minor, int patch)
    {
        this.year = year;
        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.prerelease = false;
        this.prereleaseName = null;
        this.prereleaseNumber = 0;
    }

    public PlutoVersion(int year, int major, int minor, int patch, String prereleaseName, int prereleaseNumber)
    {
        this.year = year;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        
        this.prerelease = true;
        this.prereleaseName = prereleaseName;
        this.prereleaseNumber = prereleaseNumber;
    }

    public int getYear()
    {
        return this.year;
    }

    public int getMajor()
    {
        return this.major;
    }

    public int getMinor()
    {
        return this.minor;
    }

    public int getPatch()
    {
        return this.patch;
    }

    public boolean isPrerelease()
    {
        return this.prerelease;
    }

    public String getPrereleaseName()
    {
        return this.prereleaseName;
    }

    public int getPrereleaseNumber()
    {
        return this.prereleaseNumber;
    }

    @ConstantExpression
    public static PlutoVersion of(@Nonnull String str)
    {
        assert !str.isBlank();

        assert str.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+([+\\-][a-zA-Z0-9]+(\\.[0-9]+)?)?");

        var parts = str.split("[+\\-]");

        assert parts.length == 1 || parts.length == 2;

        var versionParts = parts[0].split("\\.");

        assert versionParts.length == 4;

        if (parts.length == 2)
        {
            var prereleaseParts = parts[1].split("\\.");

            assert prereleaseParts.length == 1 || prereleaseParts.length == 2;

            int prereleaseNumber;

            if (prereleaseParts.length == 2)
                prereleaseNumber = Integer.parseInt(prereleaseParts[1]);
            else
                prereleaseNumber = 0;

            String prereleaseName = prereleaseParts[0];

            return new PlutoVersion(
                    Integer.parseInt(versionParts[0]),
                    Integer.parseInt(versionParts[1]),
                    Integer.parseInt(versionParts[2]),
                    Integer.parseInt(versionParts[3]),
                    prereleaseName,
                    prereleaseNumber);
        }

        return new PlutoVersion(
                Integer.parseInt(versionParts[0]),
                Integer.parseInt(versionParts[1]),
                Integer.parseInt(versionParts[2]),
                Integer.parseInt(versionParts[3]));
    }

    @Override
    public String toString()
    {
        if (prerelease)
            return String.format("%d.%d.%d.%d-%s.%d",
                    this.year,
                    this.major,
                    this.minor,
                    this.patch,
                    this.prereleaseName,
                    this.prereleaseNumber);
        else
            return String.format("%d.%d.%d.%d",
                    this.year,
                    this.major,
                    this.minor,
                    this.patch);
    }

    @Override
    public int compareTo(@Nonnull PlutoVersion o)
    {
        int yearDiff = this.year - o.year;
        if (yearDiff != 0)
            return yearDiff;

        int majorDiff = this.major - o.major;
        if (majorDiff != 0)
            return majorDiff;

        int minorDiff = this.minor - o.minor;
        if (minorDiff != 0)
            return minorDiff;

        int patchDiff = this.patch - o.patch;
        if (patchDiff != 0)
            return patchDiff;

        if (!this.prerelease && !o.prerelease)
            return 0;

        if (!this.prerelease)
            return 1;

        if (!o.prerelease)
            return -1;

        assert this.prereleaseName != null;
        assert o.prereleaseName != null;

        int prereleaseNameDiff = this.prereleaseName.compareTo(o.prereleaseName);

        if (prereleaseNameDiff != 0)
            return prereleaseNameDiff;

        return this.prereleaseNumber - o.prereleaseNumber;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PlutoVersion that = (PlutoVersion) o;

        if (prerelease)
            return year == that.year &&
                    major == that.major &&
                    minor == that.minor &&
                    patch == that.patch &&
                    prereleaseNumber == that.prereleaseNumber &&
                    Objects.equals(prereleaseName, that.prereleaseName);
        else
            return year == that.year &&
                    major == that.major &&
                    minor == that.minor &&
                    patch == that.patch;
    }

    @Override
    public int hashCode()
    {
        if (prerelease)
            return Objects.hash(year, major, minor, patch);
        else
            return Objects.hash(year, major, minor, patch, prereleaseName, prereleaseNumber);
    }
}
