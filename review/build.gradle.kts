plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "review"
    supportMacOS = true
    dependencies {
        android {
            main {
                implementation(libs.android.play.review)
                implementation(libs.android.play.review.ktx)
            }
        }
        common {
            main {
                // api: `ReviewManager.Builder` extends `LifecycleSubscribable`.
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
    }
}
