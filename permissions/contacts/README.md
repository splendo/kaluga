# Contacts Permissions

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  |  | ✅ |  | ✅ |

Adds a `ContactsPermission` to `Permissions`. Register this permission by calling `PermissionsBuilder.registerContactsPermission`()/`PermissionsBuilder.registerContactsPermissionIfNotRegistered()`.

## Installing
This library is available on Maven Central. You can import Kaluga Contacts Permissions as follows:

 ```kotlin
 repositories {
     // ...
     mavenCentral()
 }
 // ...
 dependencies {
     // ...
     implementation("com.splendo.kaluga.permissions:contacts:$kalugaVersion")
 }
 ```

## Please check [general permissions documentation](../core/) for full documentation
