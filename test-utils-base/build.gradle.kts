plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
    id("kotlinx-atomicfu")
}

publishableComponent("test.base")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // these are not coming from component.gradle because they need to be in the main scope
                api(kotlin("test"))
                api(kotlin("test-junit"))
                apiDependency(Dependencies.KotlinX.Coroutines.Test)

                // these dependencies make test linking slow, but Kotlin/Native cannot handle `compileOnly`
                // https://github.com/splendo/kaluga/issues/208
                api(project(":base", ""))
                api(project(":logging", ""))
            }
        }

        getByName("androidLibMain") {
        }

        getByName("jsMain") {
            dependencies {
                api(kotlin("test-js"))
            }
        }

        getByName("jvmMain") {
            dependencies {
                apiDependency(Dependencies.KotlinX.Coroutines.Test)
                apiDependency(Dependencies.KotlinX.Coroutines.Debug)
            }
        }
    }
}

android {
    dependencies {
        apiDependency(Dependencies.AndroidX.ArchCore)

        implementationDependency(Dependencies.Mockito.Core)
        implementationDependency(Dependencies.ByteBuddy.Agent)

        apiDependency(Dependencies.KotlinX.Coroutines.Test)
        apiDependency(Dependencies.KotlinX.Coroutines.Debug)
    }
}
