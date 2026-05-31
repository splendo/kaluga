plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "datetimepicker"
    dependencies {
        android {
            device {
                implementation(libs.androidx.activity.ktx)
                // Instrumented tests use `BaseLifecycleViewModel` + `KalugaViewModelActivity` from
                // `:architecture`. Production code only needs the marker types from `:lifecycle`,
                // so the architecture dep stays test-scoped.
                implementation(project(":architecture", ""))
            }
        }
        common {
            main {
                // `DateTimePicker.Builder` extends `LifecycleSubscribable`; expose it via `api`
                // so consumers can resolve the supertype without a separate `:lifecycle` dep.
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-date-time-picker", ""))
            }
        }
    }
}
