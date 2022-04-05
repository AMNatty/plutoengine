plugins {
	java
	`java-library`
}

description = "Automated shader loader and manager."

dependencies {
	api(project(":plutoengine:plutotexture"))
	api(project(":plutoengine:plutomesher"))
}