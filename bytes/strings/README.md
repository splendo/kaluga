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
An `Encoding` (`UTF_8`, `UTF_16` or `ASCII`) determines how characters are encoded. `StringEncodingSettings` combines an `Encoding` with an `EndMarking` that determines how the end of a String is marked, such as a length prefix (`LengthPrefix`), a terminating byte (`NullTerminated`, `Terminated`), a byte-stuffed region (`ByteStuffed`), a fixed number of characters padded with null characters (`FixedLength`) or no marking at all (`NoMarking`).

```kotlin
val settings = StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.UTF_8)
val bytes = "Hello".toByteArray(settings, ByteOrder.LEAST_SIGNIFICANT_FIRST)
val decoded = bytes.decodeString(settings, ByteOrder.LEAST_SIGNIFICANT_FIRST)
```

A `Char` takes `Encoding.byteSizeOf(char)` bytes: one for `ASCII`, two for `UTF_16` and one to three for `UTF_8`. Encoding a non-ASCII character with `ASCII` throws.

```kotlin
val bytes = Encoding.UTF_8.encodeChar('é', ByteOrder.LEAST_SIGNIFICANT_FIRST) // C3 A9
val decoded = bytes.decodeUTF8Char(0)
```

Chars and Strings can be added to a `ByteArrayBuilder` by importing the `add` extensions from this module.
