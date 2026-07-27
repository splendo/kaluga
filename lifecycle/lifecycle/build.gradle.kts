plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "lifecycle"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    supportJVM = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        android {
            main {
                api(androidxLibs.lifecycle.lifecycleRuntimeKtx)
                api(androidxLibs.fragment.fragment)
            }
        }
        common {
            test {
                implementation(project(":lifecycle:test", ""))
            }
        }
    }
}
