## Features targeted for 22.1.0.0-alpha.0
* `[PlutoGUI]` Initial implementation of the new font renderer
    * Full rewrite
    * High quality font rendering
        * Subpixel rendering support [?]
    * Possibly a new system for bitmap fonts
* Improve upon the support of thread-local Pluto instances
    * The long term goal is to allow an unlimited amount of Pluto applications at any given time

## Features targeted for 22.2.0.0-alpha.0
* The stage subsystem
    * A "stage", in this context, is a set of assets bound together
      by programming logic, not necessarily a game level.
      Stage switching and asset management are handled by the engine.
    * Upon stage switch
        1. Unload unused assets
        2. Load new assets
    * Provide multiple means of stage switching
        * Three modes with the initial release
            1. Instant switch
                * Assets will be loaded and unloaded synchronously
                * The stage switch will happen in one frame
            2. Deferred switch
                * The stage will continue running until all assets load
                * Assets will load synchronously, but at a slower pace
                  to avoid frame stutter
            3. Asynchronous switch
                * Assets will be loaded in asynchronously, where applicable
                * Falls back to deferred switching for synchronous loading,
                  such as OpenGL texture upload
    * Automated asset loading
        * All asset management will eventually be handled by `PlutoCore`
            * This includes audio clips, textures, sprites
        * Add a common interface for all assets
    * Let the stage system handle audio playback
    * This API should be as neutral as possible and avoid steering towards
      game-only use
    * The stage manager should be relatively low-overhead and allow multiple
      instances
    * Allow stages to be inherited from, creating a stack-like structure
* `[PlutoAudio]` Integrate the Audio API with the Stage API