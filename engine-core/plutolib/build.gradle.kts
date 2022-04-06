import org.plutoengine.Versions

plugins {
    java
    `java-library`
}

description = "Multi-purpose utility library that can be used in basically any project."

dependencies {
    api("org.jetbrains", "annotations", "23.0.0")

    api("org.yaml", "snakeyaml", "1.28")

    api("com.fasterxml.jackson.core", "jackson-core", "2.13.2")
    api("com.fasterxml.jackson.core", "jackson-databind", "2.13.2")

    api("com.google.guava", "guava", "28.0-jre")

    api("org.joml", "joml", Versions.jomlVersion)
    api("commons-io", "commons-io", "2.6")

    api("org.apache.commons", "commons-lang3", "3.12.0")

}