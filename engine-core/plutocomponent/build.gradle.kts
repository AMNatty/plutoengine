plugins {
    java
    `java-library`
}

description = "A module acting as glue for all PlutoEngine components."

dependencies {
    api("org.apache.commons:commons-lang3:3.12.0")
    api("org.apache.commons:commons-collections4:4.4")

    api("com.google.code.findbugs:jsr305:3.0.2")
}