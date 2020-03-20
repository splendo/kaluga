plugins {
    kotlin("multiplatform")
    kotlin("xcode-compat") version "0.2.5"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    google()
    maven(url="https://kotlin.bintray.com/kotlinx")
}

kotlin {
    xcode {
        setupFramework("KotlinNativeFramework")
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {

            val ext = (gradle as ExtensionAware).extra
            var primaryIosArch = ext["ios_primary_arch"]
            val lifecycleVersion = "2.2.0"

            dependencies {
                implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
                implementation(project(":shared", "${primaryIosArch}Default"))
            }
        }
    }
}
