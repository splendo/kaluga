plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies {
    /* Uncomment these lines if you are using fragments
    val ext = (gradle as ExtensionAware).extra
    androidTestImplementation("androidx.fragment:fragment-ktx:${ext["androidx_fragment_version"]}")
    */
}

kotlin {
    sourceSets {

        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext

        commonMain {
            dependencies {
                implementation(project(":base"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils"))
            }
        }
    }
}
