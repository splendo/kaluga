plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "media.test"
    supportMacOS = true
    supportTvOS = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":media:media"))
                api(project(":base:test"))
            }
        }
    }
}
