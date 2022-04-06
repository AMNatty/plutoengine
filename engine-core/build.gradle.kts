import org.plutoengine.Versions

task("publish") {
    dependsOn(":plutoengine:plutouss2:publish")
    dependsOn(":plutoengine:plutolib:publish")
    dependsOn(":plutoengine:plutocomponent:publish")
    dependsOn(":plutoengine:plutoruntime:publish")
    dependsOn(":plutoengine:plutodisplay:publish")
    dependsOn(":plutoengine:plutotexture:publish")
    dependsOn(":plutoengine:plutomesher:publish")
    dependsOn(":plutoengine:plutoshader:publish")
    dependsOn(":plutoengine:plutoframebuffer:publish")
    dependsOn(":plutoengine:plutospritesheet:publish")
    dependsOn(":plutoengine:plutogui:publish")
    dependsOn(":plutoengine:plutoaudio:publish")
    dependsOn(":plutoengine:plutocore:publish")
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

    configure<SigningExtension> {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(the<PublishingExtension>().publications["maven"])
    }
}
