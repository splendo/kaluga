# Bytes Annotations

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

The `@BytesLayout` annotation and supporting annotations that describe a byte-packed wire layout. [serialization](../serialization/) reads these annotations at runtime to encode and decode a `@Serializable` class; the [bytes code generator](../ksp/) (applied by the [`com.splendo.kaluga.bytes.plugin`](../plugin/) Gradle plugin) reads them at compile time to generate data classes and zero-copy views.

## Installing

This library is available on Maven Central. You can import Kaluga Bytes Annotations as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:annotations:$kalugaVersion")
}
```

## The `@BytesLayout` annotation

Mark an interface with `@BytesLayout` to describe a byte-packed wire layout. The [bytes code generator](../ksp/) generates implementations from it:

```kotlin
@BytesLayout
interface HeartRate {
    @Size(Length.`8_BIT`)
    @Unsigned
    val bpm: Int
}
```

By default, both a `@Serializable` data class and a zero-copy view are generated. Override this per-interface:

```kotlin
@BytesLayout(generate = [Generate.DATA])
interface DataOnly { /* ... */ }

@BytesLayout(generate = [Generate.VIEW])
interface ViewOnly { /* ... */ }
```

A generated view comes as a read-only `XView` and a writing `MutableXView`, so the read-only one cannot override a property the interface declares `var`. Such a property is a compile error unless the interface asks for a read-only view whose setter throws an `UnsupportedOperationException`:

```kotlin
@BytesLayout(readOnlySetters = ReadOnlySetters.THROW)
interface Settings {
    var level: Byte // XView.level = 1 throws; MutableXView and XData write it
}
```

Either way a `var` must be a property every writing view can rewrite in place, of a fixed size, not optional and not deciding which other properties are present; a DATA-only layout can declare any property `var`.

## Annotations by purpose

### Sizing

Control the number of bytes used to encode numeric fields.

- `@Size(Length.`8_BIT` | `16_BIT` | `24_BIT` | `32_BIT` | `40_BIT` | `48_BIT` | `64_BIT`)` — Fixed size in bytes. Repeatable to let the encoder pick the smallest that fits, storing a flag to indicate which size was used.
- `@Unsigned` — Interpret a numeric field as unsigned (default is signed).
- `@Scalar(multiplier, decimalExponent, binaryExponent, offset)` — Scale a numeric value before encoding: `scaled = value * multiplier * 2^binaryExponent * 10^decimalExponent + offset`.
- `@Range(min, max)` — Map a `Double` or `Float` proportionally across the unsigned range of the wire integer. Useful for bounded fields like humidity (0–100%).
- `@MedFloat` — Encode as IEEE 11073 medical float format (MedFloat16 or MedFloat32).

### Flags / Presence

Control how nullable fields and boolean flags are encoded in a packed flag header.

- `@FlagIndex(index)` — Store a boolean (or the null-presence flag of a nullable field) at bit position `index` in the flag header.
- `@FlagWidth(bits)` — Allocate `bits` bits for a flag (default 1). Used when an integer field is packed directly into flag bits.

An integer with `@FlagIndex` and `@FlagWidth` but no `@Size` is packed straight into the flag bits. Like any number it is signed unless its type is unsigned (`UByte`, `UShort`, `UInt`, `ULong`) or it carries `@Unsigned`, and a signed one is two's complement within its bits: `BytesFormat` sign-extends it when decoding, so a 2-bit `Byte` reads `2` back as `-2`, and refuses to encode a value outside `-2..1`. A field narrower than a byte is hardly ever meant that way, so a layout with a signed one is rejected, by `BytesFormat` when it first encodes or decodes the type and by the [bytes code generator](../ksp/) at compile time; declare it with the unsigned type or `@Unsigned`, which holds `0..3` in 2 bits. From 8 bits up a signed type is accepted and keeps its sign.
- `@PresentWhenAllSet(vararg indices: Int)` — A nullable field is present only when all listed flag bit positions are set. The field reserves no flag bit itself; the listed bits belong to other properties or flags.

### Numbers

Control numeric encoding options.

- `@Unsigned` — Encode as unsigned instead of signed.
- `@Scalar(...)` — Apply a scaling transformation.
- `@Range(...)` — Map to a bounded range.
- `@MedFloat` — Use IEEE 11073 medical float encoding.
- `@Size(...)` — Specify byte length(s).

### Strings

Control how strings are terminated and encoded.

- `@Encoded(Encoding.UTF_8 | ASCII | ...)` — Specify the character encoding (default UTF-8). A `Char` has a fixed size, so it takes ASCII (the default for a `Char`), ISO-8859-1 (Latin-1) or UTF-16; UTF-8 is rejected for it, so use a `String` for UTF-8 text.
- `@LengthPrefix(lengthAsShort, canOverflow, sentinel)` — Prefix the string with a length byte (or short if `lengthAsShort = true`). Supports overflow detection if `canOverflow = true`. On a sealed class it makes records of the subtypes, see [Enums and polymorphism](#enums-and-polymorphism).
- `@NullTerminated` — Terminate the string with a null byte (0x00) instead of a length prefix. The string must not contain null bytes.
- `@Unsized` — Consume all remaining bytes (except checksum/postfix). No other field may follow, and no other item may follow an item of a collection that ends in one, unless that item is a type-length-value record.
- `@ByteStuffed` / `@ByteStuffedXor` / `@ByteStuffedSlip` / `@ByteStuffedCobs` — Byte-stuff the string so a delimiter never appears literally in the content. The delimiter schemes (`Xor`, `Slip`, `Cobs`) terminate with their own delimiter; `@ByteStuffed` has none and needs a `@Terminal`.
- `@Terminal(terminator)` — Terminate with a custom byte instead of 0x00. With `@ByteStuffed` it must be one of the bytes the scheme escapes (`0xF0..0xF2` by default); a delimiter scheme rejects it, so choose its terminator through the scheme (`@ByteStuffedXor(delimiter = ...)`, `@ByteStuffedSlip(end = ...)`). Without stuffing the content must not contain it.

### Collections

Control how lists and maps are prefixed, terminated and their items/keys/values encoded.

- `@Size(...)` — Length prefix size (byte, short, etc.).
- `@LengthPrefix(...)` — Length prefix behavior (as with strings). Combined with `@FlagIndex` on a List/Map, packs the count into flag header bits instead of writing it into the body.
- `@Unsized` — Consume remaining bytes.
- `@NullIfEmpty` — Encode an empty list as null instead of encoding a zero count. Without it a nullable empty list is present, with a zero count.
- `@NullTerminated` — Terminate with 0x00 instead of a length prefix.
- `@Count(count)` — Always exactly `count` items (entries for a map), with no length prefix or terminator, so the collection can sit anywhere in a structure; with fixed-size items its size is fixed too. Encoding another number of items throws `UnexpectedItemCount`. Cannot be combined with another end marking, `@Size` or `@NullIfEmpty`.
- `@DeltaCoded(step)` — Store a list of integers as its first item, then a signed `step`-sized difference from each item to the next, added modulo 2 to the power of an item's size, so a step wraps around as an item does. The first item has the size and signedness of an item (its type, `@ItemSize`, `@ItemUnsigned`), in the items' byte order. Like `@Unsized` it runs to the end of what holds it, a type-length-value record or the whole value, so nothing can follow it; no bytes is an empty list, and bytes that are not a first item and whole steps do not decode. Encoding throws when an item or a step does not fit, see [Delta-coded lists](#delta-coded-lists).
- `@ByteStuffed` / `@ByteStuffedXor` / `@ByteStuffedSlip` / `@ByteStuffedCobs` — Byte-stuff the collection content.
- `@Terminal(terminator)` — Terminate with a custom byte, under the same rules as for strings.
- `@ItemSize(...)` — Fixed size for each item.
- `@ItemByteOrder(order)` — Byte order for items.
- `@KeyByteOrder(order)` — Byte order for map keys.
- `@ValueByteOrder(order)` — Byte order for map values.
- `@KeyEncoded(encoding)` — Character encoding for string keys.
- `@ValueNullTerminated` — Null-terminate string values in a map.

### Delta-coded lists

A sensor that samples faster than it can send stores most samples as the small difference from the sample before. Here a record of signed 16-bit samples holds the first one, then a signed byte per further sample, to the end of the bytes its length counts:

```kotlin
@BytesLayout
@LengthPrefix
sealed interface Packet {
    @BytesLayout
    @SerializedByteValue(0x01)
    interface Raw : Packet {
        @Unsized val samples: List<Short>
    }

    @BytesLayout
    @SerializedByteValue(0x02)
    interface Waveform : Packet {
        @DeltaCoded(Length.`8_BIT`) val samples: List<Short>
    }
}

// 02 04 10 00 02 FD is a Waveform of 16, 18 and 15: 0x0010, then +2, then -3.
```

`@DeltaCoded` takes the items from the `List` or primitive array it annotates as they are, so the encoder never decides on its own between differences and items: a protocol that sends either has a record type for each. The sample count follows from the bytes, `1 + (bytes - first item) / step`. With an `Int` list, ``@ItemSize(Length.`16_BIT`)`` makes the first item 16 bits, and `@ItemUnsigned` makes it count from 0 to 65535, a step past 65535 wrapping to 0.

It applies to a `List`, `ShortArray`, `IntArray` or other array of integers of one size whose items are not nullable, not to a `Set`, which would drop the items a step of 0 repeats, in a frame accumulated least significant first (big-endian items take `@ItemByteOrder` or `@ByteOrder(excludeStructure = true)`), and cannot be nullable or combined with another end marking or `@NullIfEmpty`. Generated views read `List<Short>`, `List<Int>` and `List<Long>` items as they go, see [ksp](../ksp/#delta-coded-lists).

### Framing and structure

Control the overall structure of the encoded bytes.

- `@Prefix(byteArray)` — Prepend these bytes (before any body fields).
- `@Postfix(byteArray)` — Append these bytes (after any body fields, but after checksum).
- `@Checksum(width, polynomial, init, xorOut, reflectIn, reflectOut)` — Compute a CRC over the body and append it (between body and postfix). On decode, validate if `BytesFormat.validateChecksum = true`.
- `@Reserved(before, after)` — Skip/write padding bytes around this field. Unlike `@Postfix`, reserved bytes are not validated on decode — any content is silently discarded. An absent nullable field has no padding. Padding after a field that runs to the end of what holds it, such as an `@Unsized` one, is data after it, which encoding refuses.
- `@ByteOrder(ByteOrder.LEAST_SIGNIFICANT_FIRST | MOST_SIGNIFICANT_FIRST, excludeStructure)` — Specify byte order for this field or structure. When `excludeStructure = true` on a class, the byte order applies to field values but not the class's sequential accumulation direction.

### Enums and polymorphism

Control how enum and sealed class variants are identified on the wire.

- `@SerializedByteValue(vararg value: Byte)` — Use a specific byte sequence (instead of the serialName) to identify this variant. All variants must use the same byte length.
- `@ByOrdinal(offset, size)` — Encode an enum by its ordinal (optionally offset and with a custom byte length). Variants can still override with `@SerializedByteValue`.
- `@SizePolymorphic` — Identify sealed class subtypes by their payload size instead of a discriminator byte. Subtypes are fixed-width, except at most one variable-size subtype is allowed as a catch-all fallback for sizes matching no fixed subtype.
- `@LengthPrefix(lengthAsShort)` on a sealed class — Write every subtype as a type-length-value record: its discriminator, then its length, then its bytes. The length counts the bytes after it, the subtype's whole frame; it is a byte, or 16 bits in the class's byte order with `lengthAsShort`. Decoding bounds the subtype by the length: bytes it has beyond its fields are skipped, so a later version may append fields, an `@Unsized` last property ends at the length, and fields may follow the record. Not with `@SizePolymorphic` or `canOverflow`.
- `@Fallback` — The subtype of a sealed class decoded for a discriminator no other subtype has, so a reader keeps going past a type it does not know. It is laid out like any other subtype after the discriminator (and the length, with `@LengthPrefix`): it may have a flag header, a frame of its own, and extend the interfaces the other subtypes share, so the fields every record starts with read the same for a type nobody knows, and an `@Unsized` `ByteArray` keeps the rest as it was. At most one per sealed class, with no `@SerializedByteValue`. Encoding one that holds the discriminator of another subtype throws, as it would read back as that subtype.
- `@Discriminator` — The one property of a `@Fallback` that holds the discriminator it was read for, an integer of the discriminators' size in its own byte order. It takes no bytes in the subtype: it is read from, and written to, the discriminator, so it may be declared anywhere among the properties, inherited ones included. It cannot be nullable or in the flag header, and as it takes no bytes it has no `@Reserved` bytes, nor the `@Prefix`, `@Postfix` or `@Checksum` of a value class, around it. Encoded on its own rather than through its sealed class, the fallback lays it out where it is declared, so it goes before an `@Unsized` last property.

A stream of records, each a type, a length and that many bytes, back to back to the end of a message. Every record starts with a time step whose size its flag header records, shared through a plain interface, the unknown ones included:

```kotlin
interface Timed {
    @FlagIndex(0) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`)
    val step: Int
}

@BytesLayout
@LengthPrefix
sealed interface Record {
    @BytesLayout
    @SerializedByteValue(0x20)
    interface Temperature : Record, Timed {
        val celsius: Short
    }

    @BytesLayout
    @Fallback
    interface Unknown : Record, Timed {
        @Discriminator val type: UByte
        @Unsized val value: ByteArray
    }
}

@BytesLayout
interface Chunk {
    val first: UInt
    @Unsized val records: List<Record>
}
```

`Temperature(step = 1000, celsius = -5)` is `20 05 01 E8 03 FB FF`: its type, its length, then a flag header picking a 16-bit step, the step and the temperature. `77 06 01 2C 01 AA BB CC` reads as `Unknown(step = 300, type = 0x77, value = AA BB CC)` and is written back as it was.

### Byte stuffing

Escape delimiter bytes so they never appear literally in the content.

- `@ByteStuffed(escapeByte, mask)` — CSafe-style byte stuffing. Escapes a contiguous block aligned to the high bits of `escapeByte`.
- `@ByteStuffedXor(escapeByte, delimiter, additionalEscapedBytes, xorKey)` — PPP/HDLC-style byte stuffing. XORs escaped bytes with `xorKey`.
- `@ByteStuffedSlip(end, escapeByte, escapedEnd, escapedEsc)` — SLIP (RFC 1055) byte stuffing. Escapes the frame delimiter and the escape marker itself.
- `@ByteStuffedCobs` — COBS (Consistent Overhead Byte Stuffing) byte stuffing. Eliminates 0x00 bytes with ~1 byte overhead per 254 bytes.
