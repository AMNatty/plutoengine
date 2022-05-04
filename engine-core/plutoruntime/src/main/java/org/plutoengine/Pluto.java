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

/**
 * Constants shared by all Pluto libraries.
 *
 * @since pre-alpha
 *
 * @author 493msi
 */
public class Pluto extends PlutoVersionConfig
{
    /**
     * The name of this engine.
     *
     * @since 20.2.0.0-alpha.3
     * */
    public static final String ENGINE_NAME = "PlutoEngine";

    public static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("cz.tefek.pluto.debug"));

    /**
     * The combined version string.
     *
     * @since pre-alpha
     * */
    public static final String VERSION = PlutoVersionConfig.VERSION;

    /**
     * The version object.
     *
     * @since 20.2.0.0-alpha.3
     * */
    public static final PlutoVersion VERSION_OBJ = PlutoVersion.of(VERSION);

    /**
     * The year version number, changes with breaking API changes.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_YEAR = VERSION_OBJ.getYear();

    /**
     * The major version number, changes with breaking API changes.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_MAJOR = VERSION_OBJ.getMajor();

    /**
     * The minor version number, changes with backwards-compatible API changes.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_MINOR = VERSION_OBJ.getMinor();

    /**
     * The patch number, changes with backwards-compatible fixes and patches.
     *
     * @since pre-alpha
     * */
    public static final int VERSION_PATCH = VERSION_OBJ.getPatch();

    /**
     * Denotes whether this build is a pre-release build.
     *
     * @since pre-alpha
     * */
    public static final boolean PRERELEASE = VERSION_OBJ.isPrerelease();

    /**
     * The name of this pre-release, e.g. alpha, beta, RC and similar.
     *
     * @since pre-alpha
     * */
    public static final String PRERELEASE_NAME = VERSION_OBJ.getPrereleaseName();

    /**
     * The pre-release patch number, incremented by 1 with *any* pre-release update.
     *
     * @since pre-alpha
     * */
    public static final int PRERELEASE_PATCH = VERSION_OBJ.getPrereleaseNumber();
}
