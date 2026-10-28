# Bytes MedFloat
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides the `MedFloat` IEEE 11073 medical float types.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes MedFloat as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:medfloat:$kalugaVersion")
}
```

## Usage
`MedFloat16` and `MedFloat32` represent the IEEE 11073 medical float formats commonly used in Bluetooth and health protocols. They wrap a `Double` and can be encoded to and decoded from their byte representations.

```kotlin
val bytes = MedFloat16(36.6).toByteArray()
val decoded = bytes.decodeMedFloat16(0)
```

Values can be added to a `ByteArrayBuilder` by importing the `add` extensions from this module.
