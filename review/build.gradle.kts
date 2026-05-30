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
                // api because `ReviewManager.Builder` extends `LifecycleSubscribable` (and on
                // Android, `ActivityLifecycleSubscribable`) — consumers calling `.create()` on a
                // Builder need to be able to resolve those supertypes.
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
    }
}
