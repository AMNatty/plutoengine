plugins {
    java
	`java-library`
}

description = ""

dependencies {
	api(project(":plutoengine:plutospritesheet"))
	api(project(":libra"))

	api("io.reactivex.rxjava3", "rxjava", "3.1.4")

	implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.12.3")

	implementation("org.lwjgl", "lwjgl-yoga")
	org.plutoengine.Versions.lwjglNatives.forEach {
		runtimeOnly("org.lwjgl", "lwjgl-yoga", classifier = it)
	}

	implementation("org.commonmark", "commonmark", "0.18.1")
}