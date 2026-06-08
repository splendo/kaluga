# Permissions

Request and monitor device permissions across platforms. The `core` module provides the permission framework; each per-type module adds support for one permission and is registered against the core `PermissionsBuilder`.

This is a feature group of [Kaluga](https://github.com/splendo/kaluga), containing the following modules:

| Module | Usage | Artifact | Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|---|---|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| [core](core/) | The permissions framework, used in conjunction with the per-type modules below | `com.splendo.kaluga.permissions:core` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [bluetooth](bluetooth/) | Managing Bluetooth permissions | `com.splendo.kaluga.permissions:bluetooth` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [calendar](calendar/) | Managing calendar permissions | `com.splendo.kaluga.permissions:calendar` | ✅ | ✅ |  |  |  | ✅ |  | ✅ |
| [camera](camera/) | Managing camera permissions | `com.splendo.kaluga.permissions:camera` | ✅ | ✅ |  | ✅ | ✅ | ✅ |  |  |
| [contacts](contacts/) | Managing contacts permissions | `com.splendo.kaluga.permissions:contacts` | ✅ | ✅ |  |  |  | ✅ |  | ✅ |
| [location](location/) | Managing location permissions | `com.splendo.kaluga.permissions:location` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [microphone](microphone/) | Managing microphone permissions | `com.splendo.kaluga.permissions:microphone` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [notifications](notifications/) | Managing notifications permissions | `com.splendo.kaluga.permissions:notifications` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
| [storage](storage/) | Managing storage permissions | `com.splendo.kaluga.permissions:storage` | ✅ | ✅ |  |  |  | ✅ | ✅ |  |
| [test-utils](test-utils/) | Test helpers for the Permissions modules | `com.splendo.kaluga.permissions:test` | ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |
