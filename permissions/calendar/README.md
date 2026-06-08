# Calendar Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  | ✅ |  | ✅ |

Adds a `CalendarPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerCalendarPermission`()/`PermissionsBuilder.registerCalendarPermissionIfNotRegistered()`.

## Installing
This library is available on Maven Central. You can import Kaluga Calendar Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:calendar:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
