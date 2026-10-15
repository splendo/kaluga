# Bytes Core
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides utilities for working with `ByteArray`s: hexadecimal conversion, building byte arrays from primitive values and bitwise helpers.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes Core as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:core:$kalugaVersion")
}
```

## Hex Conversion
- Convert a `ByteArray` to a hexadecimal String using `toHexString()`. An optional separator can be inserted between bytes.
- Parse a hexadecimal String back into a `ByteArray` using `decodeHex()`.
- Construct a `ByteArray` from a list of `Int` values using `bytesOf(...)`.

## ByteArrayBuilder
`ByteArrayBuilder` accumulates primitive values into a `ByteArray`. Values of every primitive type (`Byte`, `Short`, `Int`, `Long`, `Float`, `Double` and their unsigned variants) can be added, each encoded according to a configurable `ByteOrder`.
The [integer](../integer/), [medfloat](../medfloat/) and [strings](../strings/) modules add `add` overloads for their own types.

## Bitwise Utilities
Extensions such as `shl`, `shr`, `isBitSet` and `setBit` are available for `Byte`, `ByteArray` and the unsigned integer types.

## Data Converters
- Convert a `ByteArray` to a hexadecimal String using `toHexString()`.

For converting between `NSData` and `ByteArray` on iOS, see [`:base:core`](../../base/core/).
