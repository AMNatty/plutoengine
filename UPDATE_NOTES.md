## 20.2.0.0-alpha.1
* `[PlutoLib#cz.tefek.pluto.io.logger]` Refactored the Logger subsystem
  * Renamed `Logger#logException` to `Logger#log` to  match the rest
  of log methods and updated references to this method accordingly
  * Streamlined `StdOutSplitStream` and `StdErrSplitStream` into a more generalized
  `OutputSplitStream`
  * `Logger`'s output filenames now look cleaner with `log--YYYY-MM-DD--HH-MM-SS.txt`
  * `[Logger#setup]` can now throw `IOException`
    * `[PlutoCore]` As a result, `[PlutoApplication#run]` can now throw `Exception` 
* `[PlutoCore]` `[PlutoApplication]` now properly closes the `Logger` on exit
* `[PlutoLib]` Various typo fixes