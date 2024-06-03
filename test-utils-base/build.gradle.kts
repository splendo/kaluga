plugins {
    id("kaluga-library-components")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "test.base"
    dependencies {
        android {
            main {
                api(kotlin("test-junit"))
                api(libs.androidx.arch.core.testing)

                implementation(libs.mockito.core)
                implementation(libs.bytebuddy.agent)

                api(libs.kotlinx.coroutines.test)
                api(libs.kotlinx.coroutines.debug)
            }
        }
        common {
            main {
                // these are not coming from component.gradle because they need to be in the main scope
                api(kotlin("test"))
                api(libs.kotlin.test)
                api(libs.kotlinx.coroutines.test)

                api(project(":base", ""))
                api(project(":logging", ""))
            }
        }
        js {
            main {
                api(kotlin("test-js"))
            }
        }
        jvm {
            main {
                api(kotlin("test-junit"))
                api(libs.kotlinx.coroutines.test)
                api(libs.kotlinx.coroutines.debug)
            }
        }
    }
}
