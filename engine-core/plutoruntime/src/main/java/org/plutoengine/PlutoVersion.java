/*
 * MIT License
 *
 * Copyright (c) 2022 493msi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.plutoengine;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    public static PlutoVersion of(@NotNull String str)
    {
        assert !str.isBlank();

        assert str.matches("\\d+\\.\\d+\\.\\d+\\.\\d+([+\\-][a-zA-Z\\d]+(\\.\\d+)?)?");

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
    public int compareTo(@NotNull PlutoVersion o)
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

        if (o == null || this.getClass() != o.getClass())
            return false;

        PlutoVersion that = (PlutoVersion) o;

        if (this.prerelease)
            return this.year == that.year &&
                   this.major == that.major &&
                   this.minor == that.minor &&
                   this.patch == that.patch &&
                   this.prereleaseNumber == that.prereleaseNumber &&
                   Objects.equals(this.prereleaseName, that.prereleaseName);
        else
            return this.year == that.year &&
                   this.major == that.major &&
                   this.minor == that.minor &&
                   this.patch == that.patch;
    }

    @Override
    public int hashCode()
    {
        if (this.prerelease)
            return Objects.hash(this.year, this.major, this.minor, this.patch);
        else
            return Objects.hash(this.year, this.major, this.minor, this.patch, this.prereleaseName, this.prereleaseNumber);
    }
}
