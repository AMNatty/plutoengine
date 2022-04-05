plugins {
	java
	`java-library`
}

description = "The foundation module for games and apps built on top of PlutoEngine."

dependencies {
	api(project(":plutoengine:plutogui"))
	api(project(":plutoengine:plutoaudio"))
}