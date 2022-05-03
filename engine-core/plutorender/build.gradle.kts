plugins {
	java
	`java-library`
}

description = "OpenGL-based renderer for PlutoEngine."

dependencies {
	api(project(":plutoengine:plutodisplay"))
}