plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

dependencies {
    implementationDependency(Dependencies.AndroidX.Fragment)
    testImplementationDependency(Dependencies.AndroidX.FragmentKtx)
    androidTestImplementationDependency(Dependencies.AndroidX.Activity.Ktx)
}

publishableComponent("alerts")

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                implementation(project(":resources", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-alerts", ""))
            }
        }
    }
}
