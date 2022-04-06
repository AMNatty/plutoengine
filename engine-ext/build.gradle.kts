import org.plutoengine.Versions

task("publish") {
    file(".").listFiles().forEach {
        if (!it.isDirectory)
            return@forEach

        dependsOn(":plutoengine-ext:${it.name}:publish")
    }
}


subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = Versions.javaTargetVersion
        targetCompatibility = Versions.javaTargetVersion

        withJavadocJar()
        withSourcesJar()
    }

    configure<SourceSetContainer> {
        named("main") {
            tasks.withType<Jar> {
                from(allJava)
            }
        }
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }

        repositories {
            maven {
                name = "Vega"
                url = uri("https://vega.botdiril.com/")
                credentials {
                    val vegaUsername: String? by project
                    val vegaPassword: String? by project

                    username = vegaUsername
                    password = vegaPassword
                }
            }
        }
    }

    tasks.withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    configure<SigningExtension> {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(the<PublishingExtension>().publications["maven"])
    }
}
