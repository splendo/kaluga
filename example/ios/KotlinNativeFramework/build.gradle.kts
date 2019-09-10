plugins {
    kotlin("multiplatform") //version "1.3.50"
    kotlin("xcode-compat") version "0.2.3"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

kotlin {
    xcode {
        setupFramework("KotlinNativeFramework")
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {
            dependencies {
                implementation(project(":shared"))

                // use this to import modules directly from the project
//                implementation(project(":Components"))
                // or use this to import them using maven.
                implementation("com.splendo.mpp:Components-iOS:0.0.2")

            }
        }
    }
}