## Permissions

This library provide support for out-of-the-box access to device permissions.

Supported device features:
 - [Bluetooth](#bluetoothPermission)
 - [Calendar](#calendarPermission)
 - [Camera](#cameraPermission)
 - [Contacts](#contactsPermission)
 - [Location](#locationPermission) (both with and without background)
 - [Microphone](#microphonePermission)
 - [Notifications](#notificationsPermission)
 - [Storage](#storagePermission) (aka Photos on iOS)

 ## Installing
 To import this library add the Kaluga Bintray as a maven dependency `https://dl.bintray.com/kaluga/com.splendo.kaluga/`. You can then import Kaluga Permissions as follows:

 ```kotlin
 repositories {
     // ...
     maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga:permissions:$kalugaVersion")
 }
 ```

### Usage
Permissions can be requested through an instance of the `Permissions` class. This class is instantiated with a `PermissionsBuilder`, which defaults to requesting permissions for the Application Context (Android) or Main NSBundle (iOS). 
To instantiate the `PermissionsBuilder` with a Context other than Application Context use `PermissionsBuilder.withContext(context)` method. On iOS use `PermissionsBuilder.withBundle(bundle)` to instantiate the `PermissionsBuilder` with an NSBundle.
To be able to use builder, specific permission should be registered. Use `register<feature>Permission()` method of the `PermissionsBuilder`.

Permissions are modelled as a `Flowable` `State`, providing updates should the state change during observation. A permission can either be `Allowed` or `Denied`. A `Denied` permission can in turn be `Locked`, preventing the app from requesting it, or `Requestable`.

To request a permission, simply call `permissions[$type].request()`, where `$type` is the `Permission` you want to request.
This function will return true or false when the user has given or explicitly denied the permission.
This function returns false if the user denies any part of the permission, even if the other permissions are granted.
For instance the `Location` permission will be denied if background support is requested but only foreground is given.
If a request fails, a new request with a more limited scope can then be made.

```kotlin
val permissionBuilder = PermissionBuilder().apply {
    registerLocationPermission()
}
val permissions = Permissions(permissionBuilder)
val locationPermission = Permission.Location(background=true, precise=true)
launch {
    if (permissions[locationPermission].request()) {
        // Permission granted
    }
}
```