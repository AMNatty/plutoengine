package org.plutoengine

import org.gradle.internal.os.OperatingSystem
import org.gradle.api.JavaVersion

object Versions {
    const val lwjglVersion = "3.3.0"
    val lwjglNatives = when (OperatingSystem.current()) {
        OperatingSystem.LINUX -> "natives-linux"
        OperatingSystem.WINDOWS -> "natives-windows"
        else -> throw Error("Unsupported operating system!")
    }

    const val jomlVersion = "1.10.2"
    const val steamworks4jVersion = "1.8.0"
    const val steamworks4jServerVersion = "1.8.0"

    const val versionYear = 20
    const val versionMajor = 2
    const val versionMinor = 0
    const val versionPatch = 0

    const val isPrerelease = true
    const val prereleaseName = "alpha"
    const val prerealeaseUpdate = 3

    val versionFull =
        if (isPrerelease)
            "$versionYear.$versionMajor.$versionMinor.$versionPatch-$prereleaseName.$prerealeaseUpdate"
        else
            "$versionYear.$versionMajor.$versionMinor.$versionPatch"


    val javaTargetVersion = JavaVersion.VERSION_17
}