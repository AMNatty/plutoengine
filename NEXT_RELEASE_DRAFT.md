## Features targeted for 22.2.0.0-alpha.0
* The layer subsystem
    * A "layer", in this context, is a set of assets bound together
      by programming logic.
      Layer switching and asset management are handled by the engine.
    * Layers can be stacked on top of each other and run sequentially
      from bottom to top
    * Upon layer switch
        1. Unload unused assets
        2. Load new assets
    * Provide multiple means of layer switching
        * Two modes with the initial release, asynchronous switching will come at a later date
            1. Instant switch
                * Assets will be loaded and unloaded synchronously
                * The layer switch will happen in one frame
            2. Deferred switch
                * The layer will continue running until all assets load
                * Assets will load synchronously, but at a slower pace
                  to avoid frame stutter
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

## Features targeted for an unspecified future release
* `[PlutoSpritesheet]` Expanded capabilities 
  * Support for 9-slice rendering
  * Support for animated sprite rendering
  * Support for multidirectional sprite rendering
  * A spritesheet skeleton editor

* `[PlutoRuntime]`
  * Asynchronous switch
    * Assets will be loaded in asynchronously, where applicable
    * Falls back to deferred switching for synchronous loading,
    such as OpenGL texture upload
  
* `[PlutoGUI]` A fully-fledged GUI engine
    * Improve font-rendering capabilities
        * Subpixel rendering support [?]
    * Reimplement support for bitmap fonts
* Improve upon the support of thread-local Pluto instances
    * The long term goal is to allow an unlimited amount of Pluto applications at any given time
