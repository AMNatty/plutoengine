## Features targeted for 20.2.0.0-alpha.2
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
        