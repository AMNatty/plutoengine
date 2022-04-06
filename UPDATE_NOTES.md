## 20.2.0.0-alpha.3
* `[PlutoLib]` Removed `Severity`, use `SmartSeverity` instead
* `[PlutoLib]` Removed `TextIn`, `TextOut`, `ResourceImage` and `ResourceInputStream`
* `[PlutoLib]` Made `OutputSplitStream` public as it is now reusable
* `[PlutoLib]` Added the `@ConstantExpression` annotation
* `[PlutoLib]` The `RAID#getIDOf` method now returns `OptionalInt` to avoid NPEs
* `[PlutoLib]` The transitive dependency JOML is now provided by `PlutoLib` instead of `PlutoStatic`
* `[PlutoLib]` Created a simple Color API
    * `[PlutoShader]` Added the 8-bit RGBA `Color` class as a counterpart to AWT's `Color` class
    * `[PlutoShader]` Added the `RGBA` and `RGB` single precision float color objects 
    * `[PlutoShader]` Added the respective `IRGBA` and `IRGB` read-only interfaces
    * `[PlutoShader]` Added the `HSBA` and `HSB` single precision float color objects 
    * `[PlutoShader]` Added methods to convert between HSBA, RGBA, HSB and RGB
    * `[PlutoShader]` Added the `UniformRGBA` and `UniformRGB` shader uniform types
    
Awaiting implementation:
* `[PlutoLib]` Moved `cz.tefek.pluto.io.pluto.pp` to `cz.tefek.pluto.io.plutopackage`
* `[PlutoLib]` Completely reworked the module system

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
* `[Pluto*]` Deprecated `CRLF` with `LF` in all Java source files