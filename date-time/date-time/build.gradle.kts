plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "datetime.dates"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        val luxon = libs.versions.luxon.get()
        common {
            main {
                implementation(project(":base:core", ""))
                api(project(":base:i18n", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
        js {
            main {
                implementation(npm("luxon", luxon))
            }
        }
        wasmJs {
            main {
                implementation(npm("luxon", luxon))
            }
        }
    }
}
