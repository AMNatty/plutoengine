package org.plutoengine

import org.gradle.internal.os.OperatingSystem
import org.gradle.api.JavaVersion

object Versions {
    const val lwjglVersion = "3.3.1"
    val lwjglNatives = listOf(
        "natives-linux-arm64",
        "natives-linux-arm32",
        "natives-linux",
        "natives-macos-arm64",
        "natives-macos",
        "natives-windows-arm64",
        "natives-windows",
        "natives-windows-x86"
    )

    const val jomlVersion = "1.10.2"
    const val jomlPrimitivesVersion = "1.10.0"
    const val steamworks4jVersion = "1.8.0"
    const val steamworks4jServerVersion = "1.8.0"

    const val versionYear = 22
    const val versionMajor = 2
    const val versionMinor = 0
    const val versionPatch = 0

    const val isPrerelease = true
    const val prereleaseName = "alpha"
    const val prerealeaseUpdate = 2

    val versionFull =
        if (isPrerelease)
            "$versionYear.$versionMajor.$versionMinor.$versionPatch-$prereleaseName.$prerealeaseUpdate"
        else
            "$versionYear.$versionMajor.$versionMinor.$versionPatch"


    val javaTargetVersion = JavaVersion.VERSION_17
}