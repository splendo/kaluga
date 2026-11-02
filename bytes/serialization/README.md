# Bytes Serialization

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

Runtime byte serialization using `BytesFormat`, a Kotlin Serialization [`BinaryFormat`](https://kotlinlang.org/docs/serialization.html#binary-format) that reads the [`@BytesLayout` annotations](../annotations/) to encode and decode compact binary wire formats.

## Installing

This library is available on Maven Central. You can import Kaluga Bytes Serialization as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:serialization:$kalugaVersion")
}
```

## Basic usage

Mark a `@Serializable` data class with the annotations of the [annotations](../annotations/) module to describe its wire format. (`@BytesLayout` itself only applies to the interfaces the [bytes code generator](../ksp/) generates from; a hand-written `@Serializable` class carries the layout annotations directly.)

```kotlin
@Serializable
data class HeartRate(
    @Size(Length.`8_BIT`)
    @Unsigned
    val bpm: Int,
)

// Encode to bytes, using the default format
val bytes = BytesFormat.encodeToByteArray(HeartRate.serializer(), HeartRate(bpm = 72))

// Decode from bytes
val decoded = BytesFormat.decodeFromByteArray(HeartRate.serializer(), bytes)
```

## Configuration

`BytesFormat` is a sealed class whose companion `Default` is itself a ready-to-use format — that's what `BytesFormat.encodeToByteArray`/`decodeFromByteArray` above call into. Build a configured format from it with the `BytesFormat { }` builder:

```kotlin
val format = BytesFormat {
    validateChecksum = false     // Validate @Checksum on decode (default: true)
    serializersModule = EmptySerializersModule()
}
```

## Serialization

The `BytesFormat` can be used to easily (De)Serialize Objects into ByteArrays usable by most Bluetooth-style binary protocols. By default this will serialize as follows:

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
- Use `@MedFloat` to encode numbers as a MedFloat value. Can have its sizing determined by `@Size`. When ``Length.`16_BIT` `` encodes as `com.splendo.kaluga.bytes.medfloat.MedFloat16`, when ``Length.`32_BIT` `` encodes as `com.splendo.kaluga.bytes.medfloat.MedFloat32`.
Any other `@Size` is not allowed.
- Use `@Size` to change the length of the bytes used to encode a numeric value.
When multiple are added, the smallest `Length` that fits the entire number will be used and flags will be added to the header to indicate which size was picked.
For Float/Double values, this can only be ``Length.`32_BIT` `` or ``Length.`64_BIT` ``, for `@MedFloat` it is restricted to ``Length.`16_BIT` `` and ``Length.`32_BIT` ``.
- Use `@Unsized` to mark a String or Collection as Unsized, meaning all remaining bytes (with the exception of any `@Checksum` or `@Postfix`) belong to this object.
Attempting to encode data after will lead to an exception.
- Use `@NullIfEmpty` to mark a Collection as nullable if it is empty. When null its size will not be encoded. Without it, a nullable Collection that is empty is still present and its size is encoded.
- Use `@SerializedByteValue` to change the byte identifier of an Enum or Polymorphic class. This replaces serializing its serial name as an unsized string.
- Use one of `@ByteStuffed` (CSafe-style, escapes `0xF0`-`0xF3` via `0xF3`), `@ByteStuffedXor` (PPP/HDLC-style over an explicit byte set), `@ByteStuffedSlip` (SLIP/RFC 1055, delimiter `0xC0`) or `@ByteStuffedCobs` (COBS, eliminates `0x00` with ~1 byte per 254 overhead) to byte-stuff so a delimiter never appears literally in the content (only for `LEAST_SIGNIFICANT_FIRST` byte order). On the root structure it stuffs the body and `@Checksum` while leaving a `@Prefix`/`@Postfix` frame untouched (the CSafe frame `[start flag][stuffed body+checksum][stop flag]`). On a String or Collection property it is itself a terminated marking (no `@NullTerminated` needed), stuffing the content so the terminator stays unambiguous. A delimiter scheme terminates with its own delimiter and rejects `@Terminal`: use `@ByteStuffedXor(escapeByte = …, delimiter = …)` or `@ByteStuffedCobs` (always `0x00`), and `@ByteStuffedSlip(end = …)` (`0xC0` by default). CSafe has no delimiter of its own, so `@ByteStuffed` on a String or Collection requires a `@Terminal` drawn from the bytes it escapes (`0xF0`-`0xF2` by default, not its escape byte); it cannot protect a `0x00` terminator. Without stuffing, `@Terminal(byte)` is a plain byte terminator (the encoded content must not contain that byte). Stuffing also applies to nested structures and value classes: they stuff their body and `@Checksum` within any `@Prefix`/`@Postfix`, bounded by their terminator (a delimiter scheme's delimiter, or an explicit `@Terminal`) or, when unterminated, by being the last, unsized field. A stuffed structure inside another stuffed structure is stuffed once per layer.

Equivalent flags are available to encode items in a List (e.g. `@ItemSize`) or key/values in a Map (e.g. `@KeyEncoded`, `@ValueNullTerminated`). See the [annotations](../annotations/) module for the complete reference of every annotation and its parameters.

## Enums and polymorphism

In addition to `@SerializedByteValue` above:

- `@ByOrdinal(offset, size)` — Encode enum ordinals by position instead of the serialName, with an optional offset and custom byte width. A per-variant `@SerializedByteValue` can still override this for that variant.
- `@SizePolymorphic` — Identify sealed class subtypes by their payload byte count instead of a discriminator byte. Subtypes are fixed-width, except at most one variable-size subtype is allowed as a catch-all fallback for byte counts matching no fixed subtype.

## Integration

`BytesFormat` works with Kotlin Serialization's standard `@Serializable` plugin. Its `serializersModule` carries the same Contextual and Polymorphic serialization the rest of kotlinx.serialization uses — in particular, an *open* (non-sealed) class hierarchy needs its subtypes registered there, since they aren't known from the type declaration the way a sealed hierarchy's are:

```kotlin
// An open (non-sealed) hierarchy needs its subtypes registered in the SerializersModule;
// a sealed hierarchy dispatches automatically and needs no such registration.
val format = BytesFormat {
    serializersModule = SerializersModule {
        polymorphic(Base::class) {
            subclass(A::class)
            subclass(B::class)
        }
    }
}
```

When decoding with polymorphic types, discriminators are read as byte sequences (or byte counts for `@SizePolymorphic`) instead of as embedded serialNames.
