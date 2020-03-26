plugins {
    kotlin("multiplatform")
    kotlin("xcode-compat") version "0.2.5"
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    google()
    maven(url = "https://kotlin.bintray.com/kotlinx")
}

kotlin {
    xcode {
        setupFramework("KotlinNativeFramework")
    }

    sourceSets {
        getByName("KotlinNativeFrameworkMain") {

            val ext = (gradle as ExtensionAware).extra
            var primaryIosArch = ext["ios_primary_arch"]

            dependencies {
                implementation(project(":shared", "${primaryIosArch}Default"))

                if (!(ext["exampleAsRoot"] as Boolean)) {
                    implementation(project(":location", "${primaryIosArch}Default"))
                    implementation(project(":base", "${primaryIosArch}Default"))
                    implementation(project(":logging", "${primaryIosArch}Default"))
                    implementation(project(":alerts", "${primaryIosArch}Default"))
                    implementation(project(":permissions", "${primaryIosArch}Default"))
                    implementation(project(":hud", "${primaryIosArch}Default"))
                    implementation(project(":firebase", "${primaryIosArch}Default"))
                }
            }
        }
    }
}
