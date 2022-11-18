plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

publishableComponent()

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":logging", ""))
                apiDependency(Dependencies.Stately.Common)
                apiDependency(Dependencies.Stately.Isolate)
                apiDependency(Dependencies.Stately.IsoCollections)
                apiDependency(Dependencies.Stately.Concurrency)
            }
        }
        getByName("jsMain") {
            dependencies {
                implementation(kotlin("stdlib-common", Library.kotlinVersion))
                // JavaScript BigDecimal lib based on native BigInt
                implementation(npm("@splendo/bigdecimal", "1.0.26"))
            }
        }
        getByName("jsTest") {
            dependencies {
                api(kotlin("test-js"))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
