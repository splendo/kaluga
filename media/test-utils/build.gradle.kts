plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.media"
    supportMacOS = true
    supportTvOS = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":media:media"))
                api(project(":test-utils-base"))
            }
        }
    }
}
