plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "base"

    supportJVM = true
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    appleInterop {
        main {
            create("objectObserver").apply {
                definitionFile.set(project.file("src/nativeInterop/cinterop/objectObserver.def"))
                packageName("com.splendo.kaluga.base.kvo")
                compilerOpts("-I/src/nativeInterop/cinterop")
                includeDirs {
                    allHeaders("src/nativeInterop/cinterop")
                }
            }
        }
    }
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                implementation(project(":base:bytes", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
        js {
            main {
                api(libs.kotlinx.atomicfu)
            }
        }
    }
}
