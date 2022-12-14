# Test Utils Location

This library adds support for testing the [`location` module](../location) to [`test-utils`](../test-utils-base)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Location as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-location:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `BaseLocationStateRepoBuilder`, `BaseLocationManager`, `BaseLocationManager.Builder`, and `LocationMonitor`.
