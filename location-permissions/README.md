# Location Permissions
Adds a `LocationPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerLocationPermission`()/`PermissionsBuilder.registerLocationPermissionIfNotRegistered()`.

The `LocationPermission` can be set to acquire location data more accurately or while the app is the background.

## Installing
This library is available on Maven Central. You can import Kaluga Location Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga:location-permissions:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../base-permissions) for full documentation
