# Test Utils Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

This library adds support for testing the [`permissions` modules](../core/) to [`test-utils`](../../base/test-utils/)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Permissions as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.permissions:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `PermissionStateRepo`, `PermissionsBuilder`, and `PermissionManager`.
It also adds a `DummyPermission`.
