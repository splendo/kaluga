# Bluetooth Core
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides the shared foundation for the Bluetooth modules: the common GATT attribute types (`Service`, `Characteristic`, `Descriptor`), `GattResponse`, and attribute (de)serialization via [`BytesFormat`](../../bytes/serialization/). It is consumed by both [`client`](../client/) and [`server`](../server/).

## Installing
This library is available on Maven Central. You can import Kaluga Bluetooth Core as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bluetooth:core:$kalugaVersion")
}
```

## Serialization

Bluetooth attributes are serialized using `BytesFormat`, a Kotlin Serialization binary format from the [`bytes/serialization`](../../bytes/serialization/) module that respects the annotations of the [`bytes/annotations`](../../bytes/annotations/) module. See the latter's documentation for the complete annotation reference, including `@Size`, `@Scalar`, `@MedFloat`, `@Unsigned`, `@Checksum`, `@FlagIndex`, `@LengthPrefix`, `@Encoded`, `@NullTerminated`, `@Unsized`, `@NullIfEmpty`, `@SerializedByteValue`, byte stuffing (`@ByteStuffed`, `@ByteStuffedXor`, `@ByteStuffedSlip`, `@ByteStuffedCobs`), `@Prefix`, `@Postfix`, and more.

As an Example, this is what the Bluetooth Heart Rate Characteristic looks like:

```kotlin
@Serializable
@JvmInline
value class RRInterval private constructor(
    @Size(Length.`16_BIT`)
    @Scalar(binaryExponent = 10)
    val seconds: Double,
) {
    constructor(duration: Duration) : this(duration.toDouble(DurationUnit.SECONDS))

    val duration: Duration get() = seconds.seconds
}

@Serializable
enum class SensorLocation {
    @SerializedByteValue(0x00)
    OTHER,
    @SerializedByteValue(0x01)
    CHEST,
    @SerializedByteValue(0x02)
    WRIST,
    @SerializedByteValue(0x03)
    FINGER,
    @SerializedByteValue(0x04)
    HAND,
    @SerializedByteValue(0x05)
    EAR_LOBE,
    @SerializedByteValue(0x06)
    FOOT,
}

@Serializable
data class HeartRate(
    @Size(Length.`8_BIT`)
    @Size(Length.`16_BIT`)
    @Unsigned
    val heartRate: Int,
    @FlagIndex(2)
    val contactSupported: Boolean,
    @FlagIndex(1)
    val contactDetected: Boolean = !contactSupported,
    @Unsigned
    @Size(Length.`16_BIT`)
    val energyExpended: Int? = null,
    @NullIfEmpty
    @Unsized
    val rrIntervals: List<RRInterval> = emptyList(),
)
```
