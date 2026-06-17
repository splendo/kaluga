## Bytes

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides utilities for working with `ByteArray`s: hexadecimal conversion, building byte arrays from primitive values, bitwise helpers and the `MedFloat` IEEE 11073 medical float types.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:bytes:$kalugaVersion")
}
```

## Hex Conversion
- Convert a `ByteArray` to a hexadecimal String using `toHexString()`. An optional separator can be inserted between bytes.
- Parse a hexadecimal String back into a `ByteArray` using `decodeHex()`.
- Construct a `ByteArray` from a list of `Int` values using `bytesOf(...)`.

## ByteArrayBuilder
`ByteArrayBuilder` accumulates primitive values into a `ByteArray`. Values of every primitive type (`Byte`, `Short`, `Int`, `Long`, `Float`, `Double` and their unsigned variants, as well as `Int24`/`UInt24` and `MedFloat`) can be added, each encoded according to a configurable `ByteOrder`.

## MedFloat
`MedFloat16` and `MedFloat32` represent the IEEE 11073 medical float formats commonly used in Bluetooth and health protocols. They wrap a `Double` and can be encoded to and decoded from their byte representations.

## Bitwise Utilities
Extensions such as `shl`, `shr`, `isBitSet` and `setBit` are available for `Byte`, `ByteArray` and the unsigned integer types.

## Data Converters
- Convert a `ByteArray` to a hexadecimal String using `toHexString()`.

For converting between `NSData` and `ByteArray` on iOS, see [`:base:core`](../core/).
