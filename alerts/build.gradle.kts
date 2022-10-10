plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    implement(Dependencies.AndroidX.Fragment)
    implementForTest(Dependencies.AndroidX.FragmentKtx)
}

publishableComponent()

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":resources", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
