# Bytes Strings
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library encodes `Char`s and `String`s to bytes and decodes them back.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes Strings as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:strings:$kalugaVersion")
}
```

## Usage
An `Encoding` (`UTF_8`, `UTF_16` or `ASCII`) determines how characters are encoded. `StringEncodingSettings` combines an `Encoding` with an `EndMarking` that determines how the end of a String is marked, such as a length prefix (`LengthPrefix`), a terminating byte (`NullTerminated`, `Terminated`), a byte-stuffed region (`ByteStuffed`) or no marking at all (`NoMarking`).

```kotlin
val settings = StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_8)
val bytes = "Hello".toByteArray(settings, ByteOrder.LEAST_SIGNIFICANT_FIRST)
val decoded = bytes.decodeString(settings, ByteOrder.LEAST_SIGNIFICANT_FIRST)
```

Chars and Strings can be added to a `ByteArrayBuilder` by importing the `add` extensions from this module.
