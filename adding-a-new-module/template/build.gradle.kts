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

repositories {
    google()
    mavenCentral()
}

dependencies {
    /* Uncomment this line if you are using mockito
     testImplementation("org.mockito:mockito-core:2.28.2")
    */

    /* Uncomment these lines if you are using fragments
    implementation("androidx.fragment:fragment:1.1.0")
    androidTestImplementation("androidx.fragment:fragment-ktx:1.1.0")
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
