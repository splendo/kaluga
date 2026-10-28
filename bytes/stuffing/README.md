# Bytes Stuffing
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides byte stuffing schemes.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes Stuffing as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:stuffing:$kalugaVersion")
}
```

## Usage
A `ByteStuffingScheme` escapes bytes so that a delimiter can never appear literally inside the content, with `stuff(data)` and `unstuff(data)`. `ByteStuffingScheme.CSafe`, `Xor`, `Slip` and `Cobs` are provided.
