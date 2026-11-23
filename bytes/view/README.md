# Bytes View

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

Runtime support for the zero-copy views the [bytes plugin](../plugin/) generates from `@BytesLayout` interfaces. Most users don't call this module directly — the plugin generates view code that uses it.

## Installing

This library is available on Maven Central. You can import Kaluga Bytes View as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.bytes:view:$kalugaVersion")
}
```

## Overview

Views are zero-copy implementations of `@BytesLayout` interfaces that read and write directly into a `ByteArray` without allocating intermediate objects. They use the same layout and numeric encoding as the DATA (`@Serializable`) classes, so a view and a DATA class decode to the same values.

## Key components

These helpers are marked `@InternalBytesApi` (from [layout](../layout/)): generated views opt in to them, and they follow
the generated code rather than a compatibility promise. `FixedStrideList`, `ScannedList`, `DeltaCodedShortList`,
`DeltaCodedIntList` and `DeltaCodedLongList`, which list properties return, are public.

### ViewBytes

Bounds-checked byte access for reading and writing:

- Individual bytes, bit flags, bit ranges
- Natural numbers of every width (8, 16, 24, 32, 40, 48, 64 bits) with proper sign extension for 24/40/48 bit values
- Floats, MedFloats (IEEE 11073), characters
- Byte patterns and range comparisons
- Proper error handling: throws `ByteArrayEndedBeforeSerializationCompleted` on out-of-bounds access

### ViewStrings

Read, size and write strings with support for every `BytesFormat` end marking:

- Length-prefixed strings (byte, short, or other widths)
- Null-terminated strings
- Unsized strings
- Character encodings (UTF-8, ASCII, etc.)
- Uses the same string decoder as [strings](../strings/)

### ViewCollections

Read collection counts and walk item frames:

- `FixedStrideList` — Fixed-size items (allocation-free iteration)
- `ScannedList` — Variable-size items (scans forward to find boundaries)
- Supports the same length-prefix options as strings

### ViewDeltas

Read, size and write `@DeltaCoded` lists, the first item and then the step to each next one:

- `DeltaCodedShortList`, `DeltaCodedIntList` and `DeltaCodedLongList` — The items of such a list as a `List<Short>`, `List<Int>` or `List<Long>`. `size` follows from the bytes; `forEachShort`/`forEachInt`/`forEachLong` and `copyInto` read every item in order without allocating or boxing; `get(i)` adds up the steps before item `i`, so it takes time in proportion to `i`. An iterator, a list iterator going forward, `subList`, `lastIndexOf` and `==` read the items in order, one step each; a list iterator's `previous` adds up the steps from the first item again, like `get`
- Checks that the bytes are a first item and whole steps, and that what it writes fits, as `BytesFormat` does

### ViewChecksum

Compute, store and check CRC checksums:

- Reads/writes checksum bytes between body and postfix
- Validates checksums using the same algorithm as `BytesFormat`
- Propagates write notifications for nested views via `ViewParent`/`ViewBatch` so enclosing checksums stay up to date

### ViewStuffing

Handle byte-stuffed content:

- Finds the end of byte-stuffed regions without allocating
- Unstuffs copies when needed
- Converts byte-stuffed root frames from and to the wire

### Byte order variants

Each helper has a variant for frames accumulated in `MOST_SIGNIFICANT_FIRST` byte order, which read from the end of the array.

## Allocation behavior

Fixed-size reads and writes are allocation-free. The module uses the same `NumericCodec` as `BytesFormat`, ensuring identical byte representations on every platform. The `jvmAllocationTest` task validates this by measuring actual heap bytes allocated per call (`ThreadMXBean.currentThreadAllocatedBytes`) with the JIT capped at tier 1 (`-XX:TieredStopAtLevel=1`), so C2's escape analysis can't optimize allocations away and hide a regression.
