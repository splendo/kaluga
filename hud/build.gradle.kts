plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("hud")

dependencies {
    implementationDependency(Dependencies.AndroidX.Fragment)
    debugImplementationDependency(Dependencies.AndroidX.FragmentKtx)
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
                api(project(":test-utils-hud", ""))
            }
        }
    }
}
