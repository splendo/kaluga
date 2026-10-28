# Bytes Annotations

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

Annotations that describe a byte-packed wire layout. [serialization](../serialization/) reads them at runtime to encode and decode a `@Serializable` class.

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

- `@Encoded(Encoding.UTF_8 | ASCII | ...)` — Specify the character encoding (default UTF-8). A `Char` has a fixed size, so it takes ASCII (the default for a `Char`) or UTF-16; UTF-8 is rejected for it, so use a `String` for UTF-8 text.
- `@LengthPrefix(lengthAsShort, canOverflow, sentinel)` — Prefix the string with a length byte (or short if `lengthAsShort = true`). Supports overflow detection if `canOverflow = true`.
- `@NullTerminated` — Terminate the string with a null byte (0x00) instead of a length prefix. The string must not contain null bytes.
- `@Unsized` — Consume all remaining bytes (except checksum/postfix). No other field may follow.
- `@ByteStuffed` / `@ByteStuffedXor` / `@ByteStuffedSlip` / `@ByteStuffedCobs` — Byte-stuff the string so a delimiter never appears literally in the content. The delimiter schemes (`Xor`, `Slip`, `Cobs`) terminate with their own delimiter; `@ByteStuffed` has none and needs a `@Terminal`.
- `@Terminal(terminator)` — Terminate with a custom byte instead of 0x00. With `@ByteStuffed` it must be one of the bytes the scheme escapes (`0xF0..0xF2` by default); a delimiter scheme rejects it, so choose its terminator through the scheme (`@ByteStuffedXor(delimiter = ...)`, `@ByteStuffedSlip(end = ...)`). Without stuffing the content must not contain it.

### Collections

Control how lists and maps are prefixed, terminated and their items/keys/values encoded.

- `@Size(...)` — Length prefix size (byte, short, etc.).
- `@LengthPrefix(...)` — Length prefix behavior (as with strings). Combined with `@FlagIndex` on a List/Map, packs the count into flag header bits instead of writing it into the body.
- `@Unsized` — Consume remaining bytes.
- `@NullIfEmpty` — Encode an empty list as null instead of encoding a zero count. Without it a nullable empty list is present, with a zero count.
- `@NullTerminated` — Terminate with 0x00 instead of a length prefix.
- `@ByteStuffed` / `@ByteStuffedXor` / `@ByteStuffedSlip` / `@ByteStuffedCobs` — Byte-stuff the collection content.
- `@Terminal(terminator)` — Terminate with a custom byte, under the same rules as for strings.
- `@ItemSize(...)` — Fixed size for each item.
- `@ItemByteOrder(order)` — Byte order for items.
- `@KeyByteOrder(order)` — Byte order for map keys.
- `@ValueByteOrder(order)` — Byte order for map values.
- `@KeyEncoded(encoding)` — Character encoding for string keys.
- `@ValueNullTerminated` — Null-terminate string values in a map.

### Framing and structure

Control the overall structure of the encoded bytes.

- `@Prefix(byteArray)` — Prepend these bytes (before any body fields).
- `@Postfix(byteArray)` — Append these bytes (after any body fields, but after checksum).
- `@Checksum(width, polynomial, init, xorOut, reflectIn, reflectOut)` — Compute a CRC over the body and append it (between body and postfix). On decode, validate if `BytesFormat.validateChecksum = true`.
- `@Reserved(before, after)` — Skip/write padding bytes around this field. Unlike `@Postfix`, reserved bytes are not validated on decode — any content is silently discarded. An absent nullable field has no padding.
- `@ByteOrder(ByteOrder.LEAST_SIGNIFICANT_FIRST | MOST_SIGNIFICANT_FIRST, excludeStructure)` — Specify byte order for this field or structure. When `excludeStructure = true` on a class, the byte order applies to field values but not the class's sequential accumulation direction.

### Enums and polymorphism

Control how enum and sealed class variants are identified on the wire.

- `@SerializedByteValue(vararg value: Byte)` — Use a specific byte sequence (instead of the serialName) to identify this variant. All variants must use the same byte length.
- `@ByOrdinal(offset, size)` — Encode an enum by its ordinal (optionally offset and with a custom byte length). Variants can still override with `@SerializedByteValue`.
- `@SizePolymorphic` — Identify sealed class subtypes by their payload size instead of a discriminator byte. Subtypes are fixed-width, except at most one variable-size subtype is allowed as a catch-all fallback for sizes matching no fixed subtype.

### Byte stuffing

Escape delimiter bytes so they never appear literally in the content.

- `@ByteStuffed(escapeByte, mask)` — CSafe-style byte stuffing. Escapes a contiguous block aligned to the high bits of `escapeByte`.
- `@ByteStuffedXor(escapeByte, delimiter, additionalEscapedBytes, xorKey)` — PPP/HDLC-style byte stuffing. XORs escaped bytes with `xorKey`.
- `@ByteStuffedSlip(end, escapeByte, escapedEnd, escapedEsc)` — SLIP (RFC 1055) byte stuffing. Escapes the frame delimiter and the escape marker itself.
- `@ByteStuffedCobs` — COBS (Consistent Overhead Byte Stuffing) byte stuffing. Eliminates 0x00 bytes with ~1 byte overhead per 254 bytes.
