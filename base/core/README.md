# Base Core
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides the core data types and accessors used across all Kaluga projects: threading, flowables and concurrent collections.

## Installing
This library is available on Maven Central. You can import Kaluga Base Core as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:core:$kalugaVersion")
}
```

## Threading
You can run code on the OS Main Thread by using `runOnMain`. Use the `MainQueueDispatcher` to access the Main queue of the OS.

## Data Accessors
- Use `byOrdinalOrDefault` to get an Enum value by ordinal or the default value if no such value exists.
- (Android Only) Use the `ApplicationHolder` class to get and set the current `Application`. This is useful for default access to the ApplicationContext
- (iOS Only) you can flow on Key-Value Observed values by using the `NSObject.observeKeyValueAsFlow(keyPath:options:)` method

## Data Converters
- (iOS Only) Convert `NSData` to `ByteArray` and vice versa using `toByteArray()` and `toNSData()` respectively.

For converting a `ByteArray` to a hexadecimal String and other byte utilities, see [`:base:bytes`](../bytes/).

## BufferedAsListChannel
Consuming a flow may often take longer than producing it. Kotlin Flows can handle this using `buffer` but this progressively increase the time between data being produced and data being consumed.
Kaluga offers a `BufferedAsListChannel` to buffer all data produced between consumption into a list. This allows the consumer to deal with groups of data and ideally prevent increasing delays.
The `BufferedAsListChannel` is a `Channel` that always buffers an unlimited amount of data points.

State machines (`KalugaState`/`StateRepo`) now live in [`:base:state`](../state/), and the `Decimal` type in [`:base:decimal`](../decimal/).
