import org.plutoengine.Versions

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

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
    }
}
