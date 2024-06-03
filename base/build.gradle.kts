plugins {
    id("kaluga-library-components")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "base"
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
                implementation(project(":logging", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
        js {
            main {
                implementation(npm("@splendo/bigdecimal", "1.0.26"))
            }
        }
    }
}
