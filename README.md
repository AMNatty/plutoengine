# plutoengine

![Build Status](https://github.com/493msi/plutoengine/workflows/Gradle%20Package/badge.svg)
![Maven Version](https://img.shields.io/github/v/tag/493msi/plutoengine?label=Latest%20Version)

My hobby game engine. This repository unifies all my previous Pluto repositories.


## How to use Pluto

**Quick start**

Download [the demo project](https://github.com/plutoengine/plutoengine-basic-demo) open it in IntelliJ IDEA.

**Gradle**
```groovy
repositories {
   mavenCentral()

   maven {
      name = "Vega"
      url = uri("https://vega.botdiril.com/")
   }
}


dependencies {
   implementation group: "org.plutoengine", name: "plutocore", version: "22.3.0.0-alpha.0"
}
```

**or for Gradle Kotlin DSL**
```kotlin
repositories {
   mavenCentral()

   maven {
      name = "Vega"
      url = uri("https://vega.botdiril.com/")
   }
}

dependencies {
   implementation("org.plutoengine", "plutocore", "22.3.0.0-alpha.0")
}
```

### Licensing

The code of PlutoEngine is licensed under the MIT license.

See [LICENSING_INFO](https://github.com/493msi/plutoengine/blob/master/LICENSING_INFO.txt) for further information.

### Versioning

All submodules share a version number for simplicity reasons.

Since version `20.2.0.0-alpha.0`, PlutoEngine uses
a combined version of [semantic versioning](https://semver.org/)
and [calendar versioning](https://calver.org/), the first number
denotes the year.

Therefore, the version format is always `YY.MAJOR.MINOR.PATCH-prerelease`.

*Only `major` and `year` version changes will bring breaking API changes,
**except for pre-release versions**, which may introduce breaking changes
at any time. Pre-release versions will never increment the `minor` or `patch`
version numbers.*


## Usability status of submodules

Keep in mind PlutoEngine is in alpha and all features are tentative.
The following list simply provides an overview of how likely breaking changes are to occur.

### Safe submodules
* **PlutoCore** - Stable
* **PlutoSpritesheet** - Stable, some features are unfinished
* **PlutoDisplay** - Stable, collision API nowhere near completion
* **PlutoLib** - Mostly stable
* **PlutoRender** - Stable
* **PlutoRuntime** - Mostly stable

### Unstable submodules
* **PlutoAudio** - Very tentative, work in progress
* **PlutoGUI** - Recently rewritten, the API is highly unstable, work in progress

### Extensions
* **PlutoUSS2** - Stable
* **PlutoGameObject** - Stable


## Current priorities

See [issues](https://github.com/493msi/plutoengine/issues) for details.
