plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("service")

dependencies { }

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":logging"))
                implementation(project(":base"))
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
