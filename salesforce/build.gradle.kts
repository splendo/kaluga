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
var salesforceSdkVersion = "8.1.0"


kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":architecture", ""))
//                 /* Uncomment these lines if you want to use touchlab stately for concurrency
//                 val ext = (gradle as ExtensionAware).extra
//                 implementation("co.touchlab:stately-common:${ext["stately_version"]}")
//                 implementation("co.touchlab:stately-concurrency:${ext["stately_version"]}")
//                 */
            }
        }
        getByName("androidLibMain") {
            dependencies {
                implementation(project(":base", ""))
                implementation(project(":architecture", ""))
                implementation ("com.salesforce.mobilesdk:SalesforceSDK:$salesforceSdkVersion")
                implementation ("com.salesforce.mobilesdk:SmartStore:$salesforceSdkVersion")
                implementation ("com.salesforce.mobilesdk:MobileSync:$salesforceSdkVersion")
//                 implementation ("com.salesforce.mobilesdk:SalesforceAnalytics:$salesforceSdkVersion")
            }
        }
    }
}