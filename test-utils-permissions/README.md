# Test Utils Permissions

This library adds support for testing the [`permissions` modules](../base-permissions) to [`test-utils`](../test-utils-base)

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
    implementation("com.splendo.kaluga:test-utils-permissions:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `PermissionStateRepo`, `PermissionsBuilder`, and `PermissionManager`.
It also adds a `DummyPermission`.
