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
   implementation group: "org.plutoengine", name: "plutocore", version: "22.2.0.0-alpha.2"
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
   implementation("org.plutoengine", "plutocore", "22.2.0.0-alpha.2")
}
```

### Licensing

While all code of PlutoEngine is licensed under MIT, some media in this repository
is licensed under different terms:

* Music in the **jsr-clone** demo is made by Selulance, licensed under **CC - BY 3.0**

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
 * **PlutoFramebuffer** - Stable
 * **PlutoMesher** - Stable
 * **PlutoShader** - Stable
 * **PlutoTexture** - Stable
 * **PlutoSpriteSheet** - Stable, some features are unfinished
 * **PlutoDisplay** - Stable, collision API nowhere near completion
 * **PlutoUSS2** - Stable
 * **PlutoLib** - Mostly stable
 * **PlutoRuntime** - Mostly stable

### Unstable submodules
 * **PlutoAudio** - Very tentative, work in progress
 * **PlutoGUI** - Recently rewritten, the API is highly unstable, work in progress

 
## Current priorities

See `NEXT_RELEASE_DRAFT.md` for details.

### To be fixed
[ *Features or bugs that should be fixed **ASAP*** ]
 * Implement gradient variation support for Libra fills
 * Improve code quality in PlutoGUI

### Very high priority
[ *Implemented in the current release.* ]
 * Implement the layer system and integrate all existing systems with it
 * Improve image loading capabilities, possibly rewrite PlutoLib#TPL

### High priority
[ *Implemented in the next release.* ]
 * Expand upon the Color API
    * Color mixing and blending
    * Color transformation
    * High-performance serialization
 
### Normal priority
[ *Planned for an upcoming release.* ]
 * The collision system for PlutoStatic
 
### Low priority
[ *Items not required immediately, planned to be implemented eventually.* ]
 * Allow multiple running instances of Pluto
    * Alternatively, if this deems too difficult to implement,
    prohibit the creation of more than one instance per JVM to avoid issues
 * A networking API
 * Re-add support for external mod jars to the ModLoader
    * This feature requires a full rewrite and possibly a complete overhaul
    * Mods should have limited execution levels, for example restricted file access
      or disabled native library loading (this is probably not possible)
