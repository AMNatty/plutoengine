import org.plutoengine.Versions

sourceSets {
    java {
        create("config") {
        }

        main {
            java.srcDirs("$buildDir/generated/java")
        }
    }
}

tasks {
    val generateConfigs = register("generateConfigs", Copy::class) {
        val projectVariables = mapOf("plutoVersion" to Versions.versionFull)

        inputs.properties(projectVariables)

        from("src/config/java")

        into("$buildDir/generated/java")

        expand(projectVariables)
    }

    compileJava {
        dependsOn(generateConfigs)
    }
}

dependencies {
    api(project(":plutoengine:plutolib"))
    api(project(":plutoengine:plutocomponent"))

    api(platform("org.lwjgl:lwjgl-bom:${Versions.lwjglVersion}"))

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-xxhash")
    implementation("org.lwjgl:lwjgl-zstd")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = Versions.lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-xxhash", classifier = Versions.lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-zstd", classifier = Versions.lwjglNatives)
}