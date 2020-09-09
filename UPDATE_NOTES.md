## 20.2.0.0-alpha.2
* `build.gradle` Extracted the version numbers into separate variables

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
* `[PlutoLib]` Added `ResourceAddress#openRead` and `ResourceAddress#openWrite`
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