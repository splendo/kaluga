# Bluetooth Base

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides the shared foundation for the Bluetooth modules: the common GATT attribute types (`Service`, `Characteristic`, `Descriptor`), `GattResponse`, and the `BluetoothFormat` (de)serialization framework. It is consumed by both [`client`](../client/) and [`server`](../server/).

## Installing
This library is available on Maven Central. You can import Kaluga Bluetooth Base as follows:

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
The `BluetoothFormat` can be used to easily (De)Serialize Objects into ByteArrays usable by most Bluetooth protocols. By default this will serialize as follows:

Booleans will be added to the next available byte. Up to 8 booleans in a row will fit in a single byte, their bit position determined by their order. 
Numbers will be serialized as their byte length implies (e.g. an Int is 4 bytes, a Short 2)
Strings and Collections will be prefixed with a single byte containing their length. If longer encoding will fail.
Polymorphic and Enum classes will serialize their serialName as a string with no length prefix.
When an element is nullable, a nullable flag will be added to the flags header. The position in the flags header is determined by order of the element within the object.

Multiple flags are available to change this behaviour:

- Use `@Prefix` to always add bytes to the start of the structure.
- Use `@Postfix` to always add bytes to the end of the structure.
- Use `@Checksum` to add checksum bytes (of width `Checksum.width`) between the body and the `Postfix`. Checksum will be calculated over body only.
When decoding, if `validateChecksum` is `true` the checksum will be automatically compared to the checksum in the data and throw an exception if they don't match.
- Use `@FlagIndex` to change the position of the header flag(s) to be used for storing headers.
If applied to a Boolean, the boolean will be stored as a flag instead of within the body itself.
- Use `@FlagWidth` to change the width of the flags to be used by this element. If the desired width is bigger than this width, flag will be ignored.
- Use `@ByteOrder` to change the byte order in which this element is encoded. Nested structures must have the same byte order, though primary types can change.
- Use `@LengthPrefix` to change the length prefix used in Strings or Collections.
- Use `@Encoded` to change the encoding used in Strings.
- Use `@NullTerminated` to change the end marking of a Collection of String to be determined by a null byte instead of a length prefix
- Use `@Unsigned` to encode numbers as unsigned.
- Use `@Scalar` to encode numbers as a scalar value. Length can be modified using `@Size`, defaults to ``Length.`16_BIT` `` for Floats and ``Length.`32_BIT` `` for Doubles
- Use `@MedFloat` to encode numbers as a MedFloat value. Can have its sizing determined by `@Size`. When ``Length.`16_BIT` `` encodes as `com.splendo.kaluga.base.utils.MedFloat16`, when ``Length.`32_BIT` `` encodes as `com.splendo.kaluga.base.utils.MedFloat32`.
Any other `@Size` is not allowed.
- Use `@Size` to change the length of the bytes used to encode a numeric value.
When multiple are added, the smallest `Length` that fits the entire number will be used and flags will be added to the header to indicate which size was picked.
For Float/Double values, this can only be ``Length.`32_BIT` `` or ``Length.`64_BIT` ``, for `@MedFloat` it is restricted to ``Length.`16_BIT` `` and ``Length.`32_BIT` ``.
- Use `@Unsized` to mark a String or Collection as Unsized, meaning all remaining bytes (with the exception of any `@Checksum` or `@Postfix`) belong to this object.
Attempting to encode data after will lead to an exception.
- Use `@NullIfEmpty` to mark a Collection as nullable if it is empty. When null its size will not be encoded.
- Use `@SerializedByteValue` to change the byte identifier of an Enum or Polymorphic class. This replaces serializing its serial name as an unsized string.

Equivalent flags are available to encode items in a List (e.g. `ItemSize`) or key/values in a Map (e.g. `KeyEncoded`, `ValueNullTerminated`)

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
    @FlagIndex(1)
    val contactSupported: Boolean,
    @FlagIndex(2)
    val contactDetected: Boolean = !contactSupported,
    @Unsigned
    @Size(Length.`16_BIT`)
    val energyExpended: Int? = null,
    @NullIfEmpty
    @Unsized
    val rrIntervals: List<RRInterval> = emptyList(),
)
```
