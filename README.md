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
 * **PlutoMesher** - Stable
 * **PlutoShader** - Stable
 * **PlutoSpriteSheet** - Stable, some features are unfinished
 * **PlutoStatic** - Stable, collision API nowhere near completition
 
### Unstable submodules 
 * **PlutoAudio** - Somewhat usable, unfinished
 * **PlutoLib** - Somewhat usable, requires further polish
 
### Broken submodules, do NOT use
 * **PlutoCommandParser** - Unfinished, broken, unusable
 * **PlutoDB** - Broken, unusable
 
## Current priorities

### Very high priority
 * Finishing PlutoAudio
 * Further engine restructure
 
### High priority
 * Rewriting PlutGUI
 * Finishing PlutoCommandParser
 * The stage system and automated asset loading
 
### Normal priority
 * The collision system for PlutoStatic
 
### Low priority
 * Polishing PlutoLib
 * A networking API