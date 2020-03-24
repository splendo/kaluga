plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

android {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    iosX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation("com.google.firebase:firebase-firestore:19.0.2")
                implementation("com.google.firebase:firebase-auth:17.0.0")
            }
        }
        iosX64 {
            compilations.getByName("main") {
                val firebaseCore by cinterops.creating {
                    packageName("cocoapods.FirebaseCore")
                    defFile(project.file("$projectDir/src/iosMain/c_interop/FirebaseCore.def"))
                    includeDirs.apply {
                        allHeaders("$projectDir/../example/ios/Pods/FirebaseCore/Firebase/Core/Public")
                    }
                    compilerOpts("-F$projectDir/src/iosMain/c_interop/modules/FirebaseCore-6.0.2")
                }
            }
        }
    }
    cocoapods {
        summary = "Kaluga"
        homepage = "https://kaluga.splendo.com"
    }
}
