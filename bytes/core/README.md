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

## Numeric Encoding
`Short`, `Int`, `Long`, `Float`, `Double` and their unsigned variants encode with `toByteArray(order)` or into an existing array with `copyIntoByteArray(array, offset, order)`, and decode in place with `decodeInt(octetIndex, order)` and its siblings, without copying the array. `ByteOrder.shift(index, bitsCount)` gives the bit shift of the byte at `index` in a value of `bitsCount` bits, for modules that encode their own widths. `IntLimits` holds the value ranges of the 24-, 40- and 48-bit integers.

## ByteArrayBuilder
`buildByteArray` accumulates values into a `ByteArray`. Values of every primitive type (`Byte`, `Short`, `Int`, `Long`, `Float`, `Double` and their unsigned variants) can be added, each encoded according to a configurable `ByteOrder`. In `MOST_SIGNIFICANT_FIRST` order each value is added in front of the previous ones. A `ByteArray` is added with its bytes in their own order, and consecutive `Boolean`s are packed as bits into a byte, starting with the lowest bit.

```kotlin
val bytes = buildByteArray(ByteOrder.LEAST_SIGNIFICANT_FIRST) {
    add(true)
    add(0x0102.toShort())
    add(byteArrayOf(0x0A, 0x0B))
}
// 01 02 01 0A 0B
```

The [integer](../integer/), [medfloat](../medfloat/) and [strings](../strings/) modules add `add` overloads for their own types. A module adding its own type uses `addEncoded(size) { array, offset -> }`, which writes the value straight into the builder's buffer.

## Bitwise Utilities
`isBitSet` and `setBit` are available for `Byte`, `Short`, `Int`, `Long`, their unsigned variants and `ByteArray`. `shl` and `shr` are available for `Byte`, `Short`, `UByte` and `UShort`.

For converting between `NSData` and `ByteArray` on iOS, see [`:base:core`](../../base/core/).
