# Bytes Integer
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides the 24-, 40- and 48-bit integer types `Int24`, `Int40`, `Int48` and their unsigned variants `UInt24`, `UInt40` and `UInt48`, as used by binary protocols such as Bluetooth.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes Integer as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:integer:$kalugaVersion")
}
```

## Usage
Each type wraps a standard integer and can be encoded to and decoded from its byte representation in a given `ByteOrder`:

```kotlin
val value = 42.toInt24()
val bytes = value.toByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST)
val decoded = bytes.decodeInt24(0, ByteOrder.LEAST_SIGNIFICANT_FIRST)
```

Values can be added to a `ByteArrayBuilder` by importing the `add` extensions from this module.
Bitwise extensions such as `shl`, `shr`, `isBitSet` and `setBit` are available for these types as well.
