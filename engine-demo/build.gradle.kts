import org.plutoengine.Versions

subprojects {
    apply(plugin = "java")
    apply(plugin = "application")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = Versions.javaTargetVersion
        targetCompatibility = Versions.javaTargetVersion
    }
}
