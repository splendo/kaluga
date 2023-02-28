# Permissions

This library provides support for out-of-the-box access to device permissions.

Supported device features:
 - [Bluetooth](../bluetooth-permissions)
 - [Calendar](../calendar-permissions)
 - [Camera](../camera-permissions)
 - [Contacts](../contacts-permissions)
 - [Location](../location-permissions) (both with and without background)
 - [Microphone](../microphone-permissions)
 - [Notifications](../notifications-permissions)
 - [Storage](../storage-permissions) (aka Photos on iOS)

## Installing
This library is available on Maven Central. You can import Kaluga Permissions as follows:

```kotlin
repositories {
 // ...
 mavenCentral()
}
// ...
dependencies {
 // ...
 implementation("com.splendo.kaluga:base-permissions:$kalugaVersion")
}
```

## Usage
Permissions can be requested through an instance of the `Permissions` class. This class is instantiated with a `PermissionsBuilder`, which defaults to requesting permissions for the Application Context (Android) or Main NSBundle (iOS).
A custom `PermissionContext` can be provided.
To be able to use builder, specific permission should be registered. Use the `register<feature>Permission()` (single use only) or `register<feature>PermissionIfNotRegistered()` (multiple uses) method of the `PermissionsBuilder`.

Permissions are modelled as a `Flow` of `State`, providing updates should the state change during observation. A permission can either be `Allowed` or `Denied`. A `Denied` permission can in turn be `Locked`, preventing the app from requesting it, or `Requestable`.

To request a permission, simply call `permissions[$type].request()`, where `$type` is the `Permission` you want to request.
This function will return true or false when the user has given or explicitly denied the permission.
This function returns false if the user denies any part of the permission, even if the other permissions are granted.
For instance the `Location` permission will be denied if background support is requested but only foreground is given.
If a request fails, a new request with a more limited scope can then be made.

```kotlin
val permissionBuilder = PermissionBuilder().apply {
    registerLocationPermissionIfNotRegistered()
}
val permissions = Permissions(permissionBuilder)
val locationPermission = Permission.Location(background=true, precise=true)
launch {
    if (permissions[locationPermission].request()) {
        // Permission granted
    }
}
```

## Testing
Use the [`test-utils-permissions` module](../test-utils-permissions) to get a mockable `PermissionBuilder`, `PermissionManager`, or `PermissionStateRepo`.
