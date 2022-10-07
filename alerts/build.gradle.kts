plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

group = Library.group
version = Library.version

dependencies {

    val ext = (gradle as ExtensionAware).extra
    val androidx_fragment_version: String by ext

    implementation(Dependencies.AndroidX.Fragment.notation)
    testImplementation(Dependencies.AndroidX.FragmentKtx.notation)
}

commonComponent()

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":resources", ""))
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
