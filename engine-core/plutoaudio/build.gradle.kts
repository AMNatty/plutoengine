import org.plutoengine.Versions

plugins {
	java
	`java-library`
}

description = "PlutoEngine's sound subsystem."

dependencies {
	api(project(":plutoengine:plutodisplay"))

	api("org.lwjgl:lwjgl-openal")

	runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = Versions.lwjglNatives)
}