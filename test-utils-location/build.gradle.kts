plugins {
    kotlin("multiplatform")
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
        commonMain {
            dependencies {
                api(project(":location"))
                api(project(":test-utils-permissions"))
            }
        }
        commonTest {
            dependencies {
            }
        }
    }
}
