plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "alerts"
    dependencies {
        android {
            main {
                implementation(libs.androidx.fragment)
            }
            test {
                implementation(libs.androidx.fragment.ktx)
            }
            device {
                implementation(libs.androidx.activity.ktx)
                // Instrumented tests use `BaseLifecycleViewModel` + `KalugaViewModelActivity` from
                // `:architecture` to drive lifecycle interaction. Production code only needs the
                // marker types from `:lifecycle`, so the architecture dep stays test-scoped.
                implementation(project(":architecture", ""))
            }
        }
        common {
            main {
                // `AlertPresenter.Builder` extends `LifecycleSubscribable`; expose it via `api` so
                // consumers can resolve the supertype without a separate `:lifecycle` dep.
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                implementation(project(":resources", ""))
            }
            test {
                implementation(project(":test-utils-alerts", ""))
            }
        }
    }
}
