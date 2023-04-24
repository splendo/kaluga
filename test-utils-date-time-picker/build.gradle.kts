plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("test.datetimepicker")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":test-utils-architecture"))
                api(project(":date-time-picker"))
            }
        }
    }
}
