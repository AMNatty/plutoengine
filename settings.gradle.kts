rootProject.name = "plutoengine-sdk"

include("plutoengine",
        "plutoengine-demos")

project(":plutoengine").projectDir = file("./engine-core")
project(":plutoengine-demos").projectDir = file("./engine-demo")

include ("plutoengine:plutouss2",
         "plutoengine:plutolib",
         "plutoengine:plutocomponent",
         "plutoengine:plutoruntime",
         "plutoengine:plutodisplay",
         "plutoengine:plutotexture",
         "plutoengine:plutomesher",
         "plutoengine:plutoshader",
         "plutoengine:plutoframebuffer",
         "plutoengine:plutospritesheet",
         "plutoengine:plutogui",
         "plutoengine:plutoaudio",
         "plutoengine:plutocore")

include("plutoengine-demos:basic-application")
