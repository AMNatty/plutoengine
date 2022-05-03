plugins {
    id("edu.sc.seis.launch4j") version "2.5.3"
}

application {
    mainClass.set("cz.tefek.srclone.Main")
}

launch4j {
    mainClassName = "cz.tefek.srclone.Main"
    bundledJrePath = "jre-windows"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "-Dcz.tefek.pluto.debug=true",
        "-Dorg.lwjgl.util.Debug=true"
    )
}

distributions {
    main {
        contents {
            from("mods") {
                into("mods")
            }
        }
    }
}

dependencies {
    implementation(project(":plutoengine:plutocore"))
}