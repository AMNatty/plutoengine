import org.plutoengine.Versions

plugins {
	java
	`java-library`
}

description = "PlutoEngine's sound subsystem."

dependencies {
	api(project(":plutoengine:plutodisplay"))

	api("org.lwjgl:lwjgl-openal")

	org.plutoengine.Versions.lwjglNatives.forEach {
		runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = it)
	}
}