# Test Utils

Convenience library to import all Test Utils modules. It is recommended to import only test modules that are required for testing.

Modules:
 - [Base](../test-utils-base) provides support for UIThreadTesting and Mocking.
 - [Alerts](../test-utils-alerts) provides support for mocking the [`alerts` module](../alerts)
 - [Architecture](../test-utils-architecture) provides support for mocking the [`architecture` module](../architecture)
 - [Bluetooth Base](../test-utils-bluetooth-base) provides support for mocking the [`bluetooth-base` module](../bluetooth-base)
 - [Bluetooth Client](../test-utils-bluetooth-client) provides support for mocking the [`bluetooth-client` module](../bluetooth-client)
 - [Bluetooth Server](../test-utils-bluetooth-server) provides support for mocking the [`bluetooth-server` module](../bluetooth-server)
 - [HUD](../test-utils-hud) provides support for mocking the [`hud` module](../hud)
 - [Keyboard](../test-utils-keyboard) provides support for mocking the [`keyboard` module](../keyboard)
 - [Koin](../test-utils-koin) adds support for testing with [Koin](https://www.insert-koin.io)
 - [Lifecycle](../test-utils-lifecycle) provides Activity-cached lifecycle helpers for the [`lifecycle` module](../lifecycle)
 - [Location](../test-utils-location) provides support for mocking the [`location` module](../location)
 - [Permissions](../test-utils-permissions) provides support for mocking the [`permissions` modules](../base-permissions)
 - [Resources](../test-utils-resources) provides support for mocking the [`resources` module](../resources)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils:$kalugaVersion")
}
```
