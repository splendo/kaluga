plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

publishableComponent("test.koin")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":test-utils-base"))
                api(project(":test-utils-architecture"))
                apiDependency(Dependencies.Koin.Core)
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
