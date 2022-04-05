plugins {
	java
	`java-library`
}

description = "A library to manage, store and draw sprites."

dependencies {
	api(project(":plutoengine:plutoframebuffer"))
	api(project(":plutoengine:plutoshader"))
}