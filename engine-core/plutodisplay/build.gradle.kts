import org.plutoengine.Versions

plugins {
	java
	`java-library`
}

description = ""

dependencies {
	api(project(":plutoengine:plutoruntime"))

	api("org.lwjgl", "lwjgl")
	api("org.lwjgl", "lwjgl-glfw")
	api("org.lwjgl", "lwjgl-opengl")
	api("org.lwjgl", "lwjgl-stb")

	org.plutoengine.Versions.lwjglNatives.forEach {
		runtimeOnly("org.lwjgl", "lwjgl", classifier = it)
		runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = it)
		runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = it)
		runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = it)
	}

	api("com.code-disaster.steamworks4j", "steamworks4j", Versions.steamworks4jVersion)
	api("com.code-disaster.steamworks4j", "steamworks4j-server", Versions.steamworks4jServerVersion)
}