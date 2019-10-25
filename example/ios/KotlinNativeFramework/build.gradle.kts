plugins {
    kotlin("multiplatform")
    kotlin("xcode-compat") version "0.2.3"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/pocketbyte/hydra/")
}

kotlin {
    xcode {
        setupFramework("KotlinNativeFramework")
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {

            val ext = (gradle as ExtensionAware).extra

            val singleSet = ext["ios_one_sourceset"] as Boolean
            var iosArch = ext["ios_arch"]
            val orgArch = iosArch

            if (singleSet)
                iosArch = "ios"

            dependencies {

                implementation(project(":shared", "${iosArch}Default"))

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":Components", "${iosArch}Default"))
                    implementation(project(":alerts", "${iosArch}Default"))
                    implementation(project(":logging", "${iosArch}Default"))
                    implementation(project(":permissions", "${iosArch}Default"))
                } else {
                    val libraryVersion = ext["library_version"]
                    implementation("com.splendo.kaluga:Components-$orgArch:$libraryVersion")
                    implementation("com.splendo.kaluga:logging-$orgArch:$libraryVersion")
                    implementation("com.splendo.kaluga:alerts-$orgArch:$libraryVersion")
                    implementation("com.splendo.kaluga:permissions-$orgArch:$libraryVersion")
                }

            }
        }
    }
}