# Base CRC
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library calculates Cyclic Redundancy Checks (CRC) over `ByteArray`s.

## Installing
This library is available on Maven Central. You can import Kaluga Base CRC as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:crc:$kalugaVersion")
}
```

## Usage
A `CRC` computes a checksum over a `ByteArray` using `compute(data)`, returning the result as a `ULong`.

A large set of standard CRC variants is provided as ready-to-use objects, grouped by width (`CRC3`, `CRC4`, `CRC5`, `CRC6`, `CRC7`, `CRC8`, `CRC16`, `CRC32`, `CRC64`, …):

```kotlin
val checksum = CRC8.Bluetooth.compute(byteArrayOf(0x01, 0x02, 0x03))
```

Custom CRCs can be created by invoking `CRC(...)` directly, specifying the `width`, `polynomial`, `init` value, optional `xorOut`, and whether the input and/or output bits are reflected:

```kotlin
val crc = CRC(width = 16, polynomial = 0x1021u, init = 0xffffu)
val checksum = crc.compute(data)
```
