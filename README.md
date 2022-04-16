# plutoengine

My hobby game engine. This repository unifies all my previous Pluto repositories.

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
 
### Unstable submodules
 * **PlutoGUI** - Recently rewritten, the API is highly unstable
 * **PlutoRuntime** - Somewhat tentative, the module API has been rewritten and might contain bugs
 * **PlutoAudio** - Somewhat usable, unfinished
 
 
## Current priorities

See `NEXT_RELEASE_DRAFT.md` for details.

### To be fixed
[ *Features or bugs that should be fixed **ASAP*** ]
 * Implement gradient variation support for Libra fills
 * Improve code quality in PlutoGUI

### Very high priority
[ *Implemented in the current release.* ]
 * Improve image loading capabilities, possibly rewrite PlutoLib#TPL

### High priority
[ *Implemented in the next release.* ]
 * Finish PlutoAudio
    * Depends on the stage system
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
