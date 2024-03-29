## 22.3.0.0-alpha.1
* `[PlutoComponent]` Removing components using a token should have the same semantics as removing individual components
* `[PlutoComponent]` Made the addition and removal of components hookable before mount events are fired

## 22.3.0.0-alpha.0
* `[SDK]` **Combined `PlutoFramebuffer`, `PlutoMesher`, `PlutoShader` and `PlutoTexture`
  into `PlutoRender`**
  * Unified the package structure
* `[SDK]` **Added the license text to all source files to conform with the MIT license requirements**
* `plutoengine-demos/jsr-clone` Added a proper license file for the music

## 22.2.0.0-alpha.2
* `[SDK]` The libraries now always reference natives for all architectures
* `[SDK]` Replaced `NEXT_RELEASE_DRAFT.md` with [an issue tracker](https://github.com/493msi/plutoengine/issues) 
* `[PlutoAudio]` **Partial rewrite and support for managed sound effects**
* `plutoengine-demos/` **Added the `jsr-clone` demo**
* `[PlutoSpritesheet]` Renamed `TemporalSprite#getSideCount` to `getFrameCount`

## 22.2.0.0-alpha.1
* `[PlutoGUI]` **Added support for bitmap fonts**
* `[PlutoGUI]` Generalized the font renderer API
* `plutoengine-demos/` Removed third-party fonts
* `[PlutoRuntime]` The `ModLoader` component is now opaque, 
  pre-added mods are now specified when creating the token
* `[PlutoRuntime]` Added a linting annotation to `Logger#logf`
* `[PlutoRuntime]` Fixed a bug where creating relative resource paths
   from URIs was not possible
* `[PlutoLib]` Fixed `toRGBA` in `HSB` and `HSBA`
* `[PlutoLib]` Added `BasicInterpolation`
* `[PlutoSpritesheet]` Added `recolor(IRGBA)` to `RectangleRenderer2D` and `Renderer2D`
* `[PlutoSpritesheet]` Added `TemporalSprite` and `OrientedSprite`

## 22.2.0.0-alpha.0
* `[PlutoComponent]` **Added support for dependencies and strengtened type checks**
* `[PlutoComponent]` *Removed* `IComponent` as it was redundant to `AbstractComponent`
* `[Pluto*]` *Removed* JSR 305 annotations in favor of JetBrains annotations
* `[Pluto*]` *Removed* the `ConstantExpression` annotation in favor of `Contract`
* `[PlutoRuntime]` *Moved* `ThreadSensitive` to `org.plutoengine.address`
* `[PlutoAudio]` Transformed into a PlutoLocalComponent
* `[PlutoAudio]` `IAudioStream` is now `AutoCloseable`
* `[PlutoCore]` `InputBus` is now tied to a specific `Display` instead of searching for one in the Local 
* `[PlutoCore]` Separated `Mouse` and `Keyboard` from `InputBus` into child components
* `[PlutoCore]` Added an `init()` method that gets called before entering the main loop

## 22.1.0.0-alpha.1
* `plutoengine-demos/basic-application` Made the gradient in the fragment font shader rotatable

## 22.1.0.0-alpha.0
* `[PlutoMesher]` **Partial rewrite**
  * *Removed* `VecArray`
  * Reduced pointless abstraction
  * Creation and destruction no longer logged
* `[PlutoGUI]` **Complete rewrite of the GUI library**
* `[Pluto*]` **Unified the cleanup methods of all OpenGL object classes to `close`**
* `[PlutoLib]` New dependency: `joml-primitives`
* `[PlutoLib]` Now has a `module-info.java`
* `[PlutoLib]` Now uses JetBrains annotations instead of JSR 305
* `[PlutoDisplay]` Removed the `flipped` word from all buffer functions
* `[PlutoRuntime]` Fixed opening .zip filesystems
* `[PlutoShader]` Added `UniformArrayFloat`, `UniformArrayRGBA`,
                  `UniformArrayVec2`, `UniformArrayVec3`, `UniformArrayVec4`
* `[PlutoRuntime]` `SmartSeverity.MODULE_CHECK` now correctly uses the standard output instead of `stderr`.

## 22.0.0.0-alpha.7
* `[PlutoRuntime]` Fixed several resource filesystem bugs

## 22.0.0.0-alpha.6
* `[PlutoSpritesheet]` Added a constructor to `PartialTextureSprite` that initializes the spritesheet to `null`

## 22.0.0.0-alpha.5
* `[PlutoRuntime]` Fixed module load ordering

## 22.0.0.0-alpha.4
* `[PlutoRuntime]` Implemented optional `ResourceFileSystem` features

## 22.0.0.0-alpha.3
* `[SDK]` **Extensions are now published via GitHub actions**
  
## 22.0.0.0-alpha.2
* `[SDK]` **Created a new extension project category**
* `[PlutoUSS2]` **Now an extension**
  * `PlutoLib` no longer depends on `PlutoUSS2`
* `[PlutoGameObject]` **Reimplemented `RAID` as a Pluto extension**

## 22.0.0.0-alpha.1
* `[SDK]` Jar sources and JavaDoc are now published 
* `[PlutoRuntime]` `Mod` objects now properly contain the version number

## 22.0.0.0-alpha.0
* Version bumped to 2022
* `[SDK]` Maven version is now properly set again

## 20.2.0.0-alpha.3
* `[SDK]` Restructured the repository
  * All build scripts are now written in Kotlin
  * **Added runnable examples**
  * **Upgraded to Java 17** to take advantage of new language features and a more efficient JVM
  * **The repostiory now contains examples**
  * **Moved all classes to the `org.plutoengine` package**
  * *Removed* the prepackaged JVM wrapper introduced in the previous alpha
      as it caused numerous issues
    * In the future, JDKs will be packaged with the SDK
* `[PlutoComponent]` **Added PlutoComponent as a new module**
    * `[PlutoLib]` `PlutoLib` now depends on `PlutoComponent`
* `[PlutoUSS2]` **Added USS2 as a new module**
    * `[PlutoLib]` `PlutoLib` now depends on `PlutoUSS2`
* `[PlutoLib]` **Greatly simplified the API and moved PlutoEngine specific classes to `PlutoRuntime`**
  * ***Moved* the module system to `PlutoRuntime`**
    * *Removed* `ResourceSubscriber`,
  * *Removed* `cz.tefek.pluto.io.pluto.pp`
  * *Removed* `RAID`
  * *Moved* `Logger`, `OutputSplitStream` to `PlutoRuntime`
  * *Removed* `Severity`, use `SmartSeverity` instead
  * *Removed* `TextIn`, `TextOut`, `ResourceImage` and `ResourceInputStream`
    * Use Java's NIO instead
  * *Removed* `StaticPlutoEventManager` as the implementation was too obscure
      * The module system now uses its own event management
      * *Removed* the `EventData` class
* `[PlutoRuntime]` **Added PlutoRuntime as a new module**
    * **Completely rewrote the module system**
      * *Removed* support for external mods as the feature needs a complete overhaul
    * **Revamped resource system now based on NIO**
    * *Moved* the logging system from `PlutoLib` to `PlutoRuntime`
    * Made `OutputSplitStream` public as it is now reusable
    * **Added the Version API**
      * Added the `IVersion` interface
        * Added support for version objects
        * As a result, all fields in `Pluto` except the version string are no longer compile-time constants
    * Added the `@ConstantExpression` annotation 
* `[PlutoDisplay]` **Renamed `PlutoStatic` to `PlutoDisplay`**
  * Added the `ModGLFW` virtual module
  * `DisplayErrorCallback` and simplified the callbacks in `Display`
* `[PlutoCommandParser]` **Module discontinued as a part of PlutoEngine, it will still be developed seprately**
* `[PlutoTexturing]` Renamed to `PlutoTexture`
    * Removed `Texture#load(String)` and `Texture#load(String, MagFilter, MinFilter, WrapMode...)`
* `[PlutoLib]` The transitive dependency JOML is now provided by `PlutoLib` instead of `PlutoStatic`
* `[PlutoLib]` Created a simple Color API
    * `[PlutoLib]` Added the 8-bit RGBA `Color` class as a counterpart to AWT's `Color` class
    * `[PlutoLib]` Added the `RGBA` and `RGB` single precision float color objects
    * `[PlutoLib]` Added the respective `IRGBA` and `IRGB` read-only interfaces
    * `[PlutoLib]` Added the `HSBA` and `HSB` single precision float color objects
    * `[PlutoLib]` Added methods to convert between HSBA, RGBA, HSB and RGB
    * `[PlutoShader]` Added the `UniformRGBA` and `UniformRGB` shader uniform types
* `[PlutoCore]` Made `PlutoApplication`'s constructor protected
* `[PlutoLib]` `MiniTimeParseException` no longer contains a hardcoded String message

## 20.2.0.0-alpha.2
* `build.gradle` Extracted the version numbers into separate variables
* `build.gradle` **[experimental]** `gradlew` should now automatically download JDK11 when needed
* `build.gradle` Updated the build scripts and added source Maven publication
* `[PlutoLib]` Renamed the `cz.tefek.pluto.eventsystem` package to `cz.tefek.pluto.event`
    * Moved all subpackages
* `[PlutoLib]` Minor code cleanup in `cz.tefek.pluto.modloader.event`
* `[Pluto]` Moved `TPL` from `cz.tefek.pluto.tpl` to `cz.tefek.pluto.io.tpl`
* `[PlutoMesher]` Renamed all occurrences of `attrib` to `attribute`
    * Renamed `VertexArray#createArrayAttrib` to `VertexArray#createArrayAttribute`
    * Renamed `VertexArray#getVertexAttribs` to `VertexArray#getVertexAttributes`
* `[PlutoCore]` Made `PlutoApplication.StartupConfig` fields private, options
  can now only be modified only through public setters
* `[PlutoLib]` Added the `ThreadSensitive` annotation
* `[PlutoLib]` Renamed `MiniTimeCouldNotBeParsedException` to `MiniTimeParseException`
* `[PlutoCore]` Refactored `InputBus` and added several convenience methods
    * `[PlutoCore]` Refactored input callbacks
* `[PlutoStatic]` Slight cleanup in the `Display` and `DisplayBuilder` classes

## 20.2.0.0-alpha.1
* `[PlutoLib#cz.tefek.pluto.io.logger]` Refactored the Logger subsystem
    * Renamed `Logger#logException` to `Logger#log` to  match the rest
      of log methods and updated references to this method accordingly
    * Streamlined `StdOutSplitStream` and `StdErrSplitStream` into a more generalized
      `OutputSplitStream`
    * `Logger`'s output filenames now look cleaner with `log--YYYY-MM-DD--HH-MM-SS.txt`
    * `[Logger#setup]` can now throw `IOException`
        * `[PlutoCore]` As a result, `[PlutoApplication#run]` can now throw `Exception`
* `[PlutoLib]` Updated JavaDoc in `ResourceAddress`, `TPL`, `TPNImage`
* `[PlutoLib]` Code cleanup in `MiniTime`, `TPL`
    * `[PlutoLib]` Deprecated `TPL#load(String)` in favor of `TPL#load(ResourceAddress)`, 
      `TPL#load(File)` and `TPL#load(Path)`
        * `[PlutoTexturing]` Deprecated the `String` variant of `Texture#load`
          to reflect this change
        * `[PlutoSpritesheet]` Removed the usage of this method
          in `DisposablePlaceholderSprite`
    * `[PlutoLib]` Added an option to flip loaded images with `TPL#loadImageSpecial`
      and added respective `TPL#loadSpecial` for every `TPL#load`
    * `[PlutoLib]` *Removed* `TPJImage`
        * `[PlutoLib]` Removed `TPL#loadPixels`
        * `[PlutoCore]` Updated `GLFWImageUtil` to remove the usage of `TPJImage`
* `[PlutoCore]` `[PlutoApplication]` now properly closes the `Logger` on exit
* `[PlutoLib]` Various typo fixes
* `[Pluto*]` Deprecated `Severity` for `SmartSeverity` and replaced all usages
* `[Pluto*]` Replaced `CRLF` with `LF` in all Java source files