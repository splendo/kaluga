plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("maven-publish")
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
    implementation("androidx.fragment:fragment:${ext["androidx_fragment_version"]}")
    androidTestImplementation("androidx.fragment:fragment-ktx:${ext["androidx_fragment_version"]}")
*/
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))

                /* Uncomment these lines if you want to use touchlab stately for concurrency
                val ext = (gradle as ExtensionAware).extra
                implementation("co.touchlab:stately-common:${ext["stately_version"]}")
                implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
                */
            }
        }
    }
}
