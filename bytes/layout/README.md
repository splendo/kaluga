# Bytes Layout

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

The core layout compilation and validation framework for byte-packed wire formats. Used internally by [serialization](../serialization/) (at runtime) and the [bytes code generator](../ksp/) (at compile time) to describe and validate layouts.

## Installing

This library is available on Maven Central. You can import Kaluga Bytes Layout as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:layout:$kalugaVersion")
}
```

## Overview

The bytes layout framework converts interface descriptions into executable wire format specifications. It bridges the gap between the developer-facing `@BytesLayout` annotations and the low-level byte encoding/decoding logic.

## Key components

`TypeShape`, `LayoutCompiler`, the layout dumps and `NumericCodec` are marked `@InternalBytesApi`, an opt-in marker
this module defines: the bytes modules and the code the [bytes plugin](../plugin/) generates call them, and they may change in any
release. Opt in with `@OptIn(InternalBytesApi::class)` only to inspect a layout, for instance through its dump.

### TypeShape

`TypeShape` describes the structure of a type: its properties, their encoding annotations, sizes, and constraints. Built recursively from a `SerialDescriptor` at runtime, or from KSP-resolved source symbols at compile time, so both routes compile to the same layout.

### LayoutCompiler

`LayoutCompiler` converts a `TypeShape` into a validated `Layout` — a fixed encoding/decoding plan. It handles:

- Resolving field positions and sizes
- Packing boolean and flag fields into the header
- Validating consistency (e.g., no duplicate flag indices, byte order agreement within nested structures)
- Computing payload sizes for polymorphic dispatch

### Layout

`Layout` represents a compiled wire format specification. It contains:

- Field positions, sizes and byte orders
- Header layout (which bits hold which flags)
- Checksum specifications
- Byte-stuffing schemes
- Validation rules for encoding/decoding

The layout is read by:
- `BytesFormat` (at runtime, to encode/decode `@Serializable` classes)
- The [bytes code generator](../ksp/) (at compile time, to generate data classes and zero-copy views, and in the views' runtime helpers)

### Layout dumps

`FieldLayout.dump()` renders a compiled `Layout` as a human-readable summary, one line per field, showing:

- Field names, types and positions
- Header bit allocations
- Size/padding information
- Discriminator and size-dispatch details for sealed and enum types

[serialization](../serialization/) exposes the same thing from a `SerialDescriptor` as `layoutDumpOf(descriptor)`, and the [bytes code generator](../ksp/) compares its compile-time dump against it to catch generator/runtime drift. Useful for debugging layout mismatches and understanding how annotated fields map to wire bytes.

### NumericCodec

`NumericCodec` encodes and decodes fixed-width numeric values in various byte orders:

- Endianness (least/most significant first)
- Bit widths (8 to 64 bits, including non-byte-aligned like 24-bit)
- Signedness (signed/unsigned)
- Scaling (multipliers, exponents, offsets)
- Medical floats (IEEE 11073 MedFloat16/32)

### Exceptions

Layout compilation can throw several exceptions to catch configuration errors. Every exception of this module is a
subclass of kotlinx's `SerializationException`, so a caller of `BytesFormat` can catch them all as one, or tell them
apart by their class:

- `FlagIndexException` — A flag bit index already claimed by a sibling property
- `InvalidByteOrderException` — Nested structures with conflicting byte orders
- `UnsupportedByteStuffing` — Byte stuffing applied to an unsupported encoding (e.g., most-significant-first)
- `InvalidSizePolymorphic` — A `@SizePolymorphic` sealed type whose subtypes can't be told apart by their byte size
- `InvalidChecksumException` — A decoded checksum doesn't match the one computed over the body
