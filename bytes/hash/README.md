# Bytes Hash
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library computes hash digests over `ByteArray`s.

## Installing
This library is available on Maven Central. You can import Kaluga Bytes Hash as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:hash:$kalugaVersion")
}
```

## Usage
`sha1()` computes the 20-byte SHA-1 digest of a `ByteArray`.
