plugins {
    java
    `java-library`
}

description = "A module acting as glue for all PlutoEngine components."

dependencies {
    api("org.jetbrains", "annotations", "23.0.0")

    implementation("org.apache.commons", "commons-lang3", "3.12.0")
    implementation("org.apache.commons", "commons-collections4", "4.4")

}