tasks.withType<Wrapper> {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.2"
}

subprojects {
    group = "cz.tefek"

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}