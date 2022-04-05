import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import org.plutoengine.Versions


val buildTime: String = ZonedDateTime.now()
                                     .withZoneSameInstant(ZoneId.of("Europe/Prague"))
                                     .toLocalDateTime()
                                     .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

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
        val projectVariables = mapOf(
            "gameVersion" to Versions.versionFull,
            "gameBuild" to buildTime
        )

        inputs.properties(projectVariables)

        from("src/config/java")

        into("$buildDir/generated/java")

        expand(projectVariables)
    }

    compileJava {
        dependsOn(generateConfigs)
    }
}

application {
    mainClass.set("cz.tefek.plutodemo.Main")
}


tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "-Dcz.tefek.pluto.debug=true",
        "-Dorg.lwjgl.util.Debug=true"
    )
}

dependencies {
    implementation(project(":plutoengine:plutocore"))
}