plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "review"
    dependencies {
        android {
            main {
                implementation(libs.android.play.review)
                implementation(libs.android.play.review.ktx)
            }
        }
        common {
            main {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
        }
    }
}
