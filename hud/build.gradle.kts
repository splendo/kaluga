plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "hud"
    dependencies {
        android {
            main {
                implementation(libs.androidx.fragment)
            }
            test {
                implementation(libs.androidx.fragment.ktx)
            }
            device {
                // Instrumented tests use `BaseLifecycleViewModel` + `KalugaViewModelActivity` from
                // `:architecture`. Production code only needs the marker types from `:lifecycle`,
                // so the architecture dep stays test-scoped.
                implementation(project(":architecture", ""))
            }
        }
        common {
            main {
                // `HUD.Builder` extends `LifecycleSubscribable`; expose it via `api` so consumers
                // can resolve the supertype without a separate `:lifecycle` dep.
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-hud", ""))
            }
        }
    }
}
