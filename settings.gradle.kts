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

include(":base")
include(":bluetooth:core")
include(":bluetooth:client")
include(":bluetooth:server")
include(":bluetooth:beacons")
include(":architecture:architecture")
include(":architecture:compose")
// Published as `com.splendo.kaluga.architecture:test`; the directory stays `test-utils` for clarity.
include(":architecture:test")
project(":architecture:test").projectDir = file("architecture/test-utils")
include(":alerts")
include(":date-time")
include(":date-time-picker")
include(":logging")
include(":hud")
include(":permissions:core")
include(":permissions:bluetooth")
include(":permissions:calendar")
include(":permissions:location")
include(":permissions:storage")
include(":permissions:notifications")
include(":permissions:contacts")
include(":permissions:microphone")
include(":permissions:camera")
include(":location")
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
include(":scientific")
include(":scientific-converters")
include(":service")
include(":system")
// Test Utils
include(":test-utils")
include(":test-utils-base")
include(":test-utils-alerts")
include(":bluetooth:test:core")
// The `:bluetooth:test` grouping project has no build script; point it at the (otherwise empty) container dir.
project(":bluetooth:test").projectDir = file("bluetooth/test-utils")
project(":bluetooth:test:core").projectDir = file("bluetooth/test-utils/core")
include(":bluetooth:test:client")
project(":bluetooth:test:client").projectDir = file("bluetooth/test-utils/client")
include(":bluetooth:test:server")
project(":bluetooth:test:server").projectDir = file("bluetooth/test-utils/server")
include(":test-utils-date-time-picker")
include(":test-utils-hud")
include(":keyboard:test")
project(":keyboard:test").projectDir = file("keyboard/test-utils")
include(":test-utils-koin")
include(":lifecycle:test")
project(":lifecycle:test").projectDir = file("lifecycle/test-utils")
include(":test-utils-location")
include(":media:test")
project(":media:test").projectDir = file("media/test-utils")
include(":permissions:test")
project(":permissions:test").projectDir = file("permissions/test-utils")
include(":resources:test")
project(":resources:test").projectDir = file("resources/test-utils")
include(":test-utils-service")
include(":test-utils-system")
