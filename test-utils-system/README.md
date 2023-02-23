# Test Utils System

This library adds support for testing the [`system` module](../system) to [`test-utils`](../test-utils-base)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Resources as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:test-utils-system:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `NetworkManager`, and `BaseNetworkStateRepoBuilder`
