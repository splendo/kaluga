plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("datetimepicker")

dependencies {
    androidTestImplementationDependency(Dependencies.AndroidX.Activity.Ktx)
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-date-time-picker", ""))
            }
        }
    }
}
