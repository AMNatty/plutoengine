rootProject.name = "plutoengine-sdk"

include("plutoengine",
        "plutoengine-ext",
        "plutoengine-demos",
        "libra")

project(":plutoengine").projectDir = file("./engine-core")
project(":plutoengine-ext").projectDir = file("./engine-ext")
project(":plutoengine-demos").projectDir = file("./engine-demo")

file("engine-core").listFiles().forEach {
    if (!it.isDirectory)
        return@forEach

    include("plutoengine:${it.name}")
}

file("engine-ext").listFiles().forEach {
    if (!it.isDirectory)
        return@forEach

    include("plutoengine-ext:${it.name}")
}

file("engine-demo").listFiles().forEach {
    if (!it.isDirectory)
        return@forEach

    include("plutoengine-demos:${it.name}")
}