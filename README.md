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
 * **PlutoGUI** - Stable, awaiting a rewrite
 * **PlutoLib** - Mostly stable, the module API still has some quirks
 * **PlutoMesher** - Stable
 * **PlutoShader** - Stable
 * **PlutoSpriteSheet** - Stable, some features are unfinished
 * **PlutoStatic** - Stable, collision API nowhere near completion
 
### Unstable submodules 
 * **PlutoAudio** - Somewhat usable, unfinished
 
### Broken submodules, do NOT use
 * **PlutoCommandParser** - Unfinished, broken, unusable
 * **PlutoDB** - Broken, unusable
 
## Current priorities

See `NEXT_RELEASE_DRAFT.md` for details.

### Very high priority
[ *Implemented in the current release.* ]
 * Rewrite the ModLoader
 * Streamline PlutoLib, remove bad APIs and improve code quality
 * The stage system and automated asset loading
 
### High priority
[ *Implemented in the next release.* ]
 * Rewrite PlutoGUI
 * Finish PlutoAudio
    * Depends on the stage system
 * Finish PlutoCommandParser
 
### Normal priority
[ *Planned for an upcoming release.* ]
 * The collision system for PlutoStatic
 * Improve image loading capabilities, possibly rewrite PlutoLib#TPL
 
### Low priority
[ *Items not required immediately, planned to be implemented eventually.* ]
 * Allow multiple running instances of Pluto
    * Alternatively, if this deems too difficult to implement,
    prohibit the creation of more than instance per VM to avoid issues
 * A networking API
 * Expand upon the Color API
    * Color mixing and blending
    * Color transformation
    * High-performance serialization