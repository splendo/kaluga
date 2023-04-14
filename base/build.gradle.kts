plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlinx-atomicfu")
}

publishableComponent("base")

dependencies {
    implementationDependency(Dependencies.KotlinX.AtomicFu)
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":logging", ""))
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
