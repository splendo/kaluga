# Permissions
Adds all supported permissions. Register permissions using `PermissionBuilder.registerAllPermissions()`/`PermissonBuilder.registerAllPermissionsNotRegistered()`

NOTE: This will add all permissions to the Manifest/Info.plist of the app. Usage is not recommended for production apps.

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
     implementation("com.splendo.kaluga:permissions:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../base-permissions) for full documentation
