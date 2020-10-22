## Features targeted for 20.2.0.0-alpha.3
* `[PlutoLib]` Completely redo the ModLoader system
    * The current implementation is a result of 5 years of feature creep 
    * Large scale API changes, however the general idea should stay the same
    * Rethink the class loader system.
* `[PlutoLib]` Redo the resource system
* `[PlutoLib]` Create a new Color API and port renderer code to it

## Features targeted for 20.2.0.0-alpha.4
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
* Improve upon the support of thread-local Pluto instances
    * The long term goal is to allow an unlimited amount of Pluto applications at any given time

## Features targeted for 20.2.0.0-alpha.5
* The initial minimal release of `[PlutoCommandParser]`