/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
 *
 * Also any new modules should be added to the build matrix in [.git/workflows]
 *
 ***********************************************/

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    includeBuild("kaluga-library-components")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Kaluga"

/* REMINDER (see header), files should be kept up to date with Gradle and the [.git/workflows] */

include(":base:core")
include(":base:bytes")
include(":base:crc")
include(":base:i18n")
include(":base:formatting")
include(":base:state")
include(":bluetooth:core")
include(":bluetooth:client")
include(":bluetooth:server")
include(":bluetooth:beacons")
include(":architecture:architecture")
include(":architecture:compose")
// Published as `com.splendo.kaluga.architecture:test`; the directory stays `test-utils` for clarity.
include(":architecture:test")
project(":architecture:test").projectDir = file("architecture/test-utils")
include(":alerts:alerts")
include(":date-time:date-time")
include(":date-time:timer")
include(":date-time-picker:date-time-picker")
include(":logging")
include(":hud:hud")
include(":permissions:core")
include(":permissions:bluetooth")
include(":permissions:calendar")
include(":permissions:location")
include(":permissions:storage")
include(":permissions:notifications")
include(":permissions:contacts")
include(":permissions:microphone")
include(":permissions:camera")
include(":location:location")
include(":keyboard:keyboard")
include(":keyboard:compose")
include(":lifecycle:lifecycle")
include(":lifecycle:compose")
include(":links")
include(":media:media")
include(":media:compose")
include(":resources:resources")
include(":resources:compose")
include(":resources:databinding")
include(":review")
include(":scientific:scientific")
include(":scientific:converters")
include(":service:service")
include(":system:system")
// Test Utils
include(":base:test")
project(":base:test").projectDir = file("base/test-utils")
include(":alerts:test")
project(":alerts:test").projectDir = file("alerts/test-utils")
include(":bluetooth:test-core")
project(":bluetooth:test-core").projectDir = file("bluetooth/test-utils/core")
include(":bluetooth:test-client")
project(":bluetooth:test-client").projectDir = file("bluetooth/test-utils/client")
include(":bluetooth:test-server")
project(":bluetooth:test-server").projectDir = file("bluetooth/test-utils/server")
include(":date-time-picker:test")
project(":date-time-picker:test").projectDir = file("date-time-picker/test-utils")
include(":hud:test")
project(":hud:test").projectDir = file("hud/test-utils")
include(":keyboard:test")
project(":keyboard:test").projectDir = file("keyboard/test-utils")
include(":architecture:test-koin")
project(":architecture:test-koin").projectDir = file("architecture/test-koin")
include(":lifecycle:test")
project(":lifecycle:test").projectDir = file("lifecycle/test-utils")
include(":location:test")
project(":location:test").projectDir = file("location/test-utils")
include(":media:test")
project(":media:test").projectDir = file("media/test-utils")
include(":permissions:test")
project(":permissions:test").projectDir = file("permissions/test-utils")
include(":resources:test")
project(":resources:test").projectDir = file("resources/test-utils")
include(":service:test")
project(":service:test").projectDir = file("service/test-utils")
include(":system:test")
project(":system:test").projectDir = file("system/test-utils")
