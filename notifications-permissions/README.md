## Notifications Permissions
Adds a `NotificationsPermission`. Register this permission by calling `PermissionsBuilder.registerNotificationsPermission`.

On Android this permission will be granted automatically.

## Installing
This library is available on Maven Central. You can import Kaluga Notifications Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga:notifications-permissions:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../base-permissions) for full documentation