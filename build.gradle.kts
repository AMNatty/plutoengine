import org.plutoengine.Versions

project.ext["isPlutoBuild"] = true;

tasks.withType<Wrapper> {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.2"
}

allprojects {
    group = "org.plutoengine"
    version = Versions.versionFull
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}