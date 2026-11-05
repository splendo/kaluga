# Bytes KSP

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|  |  | ✅ |  |  |  |  |  |

The [KSP](https://kotlinlang.org/docs/ksp-overview.html) symbol processor that powers `BytesFormat` code generation. It reads interfaces annotated with [`@BytesLayout`](../annotations/) and generates DATA and/or VIEW implementations. Being a KSP processor, it is a build-time, JVM-only artifact with no runtime library of its own.

You do not apply this processor yourself; the [bytes plugin](../plugin/) registers it automatically and passes configuration from the `bytes { }` DSL.

## DATA generation (for `@Serializable` classes)

For a layout interface `X`, the processor generates:

### XData

A `@Serializable` data class implementing `X` with all layout annotations copied. Produces the same bytes as any other implementation of `X` when encoded via `BytesFormat`.

- When `X` has no properties, `XData` is a data object (singleton)
- When `X` is sealed, `XData` is a sealed interface with one data class per subtype

### XData.LAYOUT_DUMP

The layout computed at compile time by the same `LayoutCompiler` that `BytesFormat` uses, internal to the module. Enables runtime validation: `layoutDumpOf(XData.serializer().descriptor)` must reproduce it.

Generated views call the runtime helpers of [view](../view/) and [layout](../layout/) marked `@InternalBytesApi`, and opt in to them file by file; that API follows the generated code and may change in any release.

### XData.from(x) and XData.InterfaceSerializer

- `from(x)` — Converts any implementation of `X` into an `XData` instance
- `InterfaceSerializer` — Encodes any `X` implementation, not just `XData`

### <Prefix>BytesLayouts

One internal object per package, listing every generated DATA serializer with its compile-time layout dump. Used to cross-check each one against `layoutDumpOf` on its runtime descriptor, catching drift between the KSP-computed and runtime layouts. The prefix is the `registryPrefix` processor option, which the Gradle plugin derives from the project path and the source set each KSP pass processes (`ValidationFormatCommonMain` for `:validation:format`'s `commonMain`), so the registries of different modules and source sets never clash; without the option it is `Generated`.

## VIEW generation (for zero-copy `@JvmInline value class` views)

For a layout interface `X`, the processor generates:

### XView and MutableXView

Two `@JvmInline value class`es over a `ByteArray` implementing `X`. `XView` is a read-only zero-copy view: its properties read the byte array in place and have no setters, so one array can be handed to several consumers, such as every collector of a notification, without one of them changing it for the others. `MutableXView` is the same view reading and writing `bytes` in place, with setters for the properties that can be rewritten and `edit { }`.

`XView` is read-only by its API, not enforced: `bytes` stays public, and a nested layout or list it returns is the writable `XSlice` or `FixedStrideList`, reachable by a cast, as a `List` can be cast to `MutableList`.

- `XView.encode(x)` and `MutableXView.encode(x)` — Create a view from an `X` implementation, in an array sized exactly
- `XView(field1, field2, …)` and `MutableXView(field1, field2, …)` — Create a view from individual field values, one parameter per property
- `XView.wrap(bytes, validateChecksum)` and `MutableXView.wrap(bytes, validateChecksum)` — Wrap an existing array, optionally validating prefixes, postfixes, checksums, lengths
- `XView.wrapRange(bytes, from, to, validateChecksum)` — Views a slice of a larger array; `MutableXView` has none, as it would return the same slice
- `XView.SIZE_BYTES` and `MutableXView.SIZE_BYTES` — The size of every view of a layout of a fixed size
- `xView.toMutable()` — A `MutableXView` over a copy of the bytes
- `mutableXView.asView()` — An `XView` over the same bytes, without copying; the only conversion that shares them
- `toData()` — Copies the view's bytes into an `XData` instance
- `x.toXView()` — Converts any `X` implementation into a read-only view: itself when it is one, a copy of the bytes of a `MutableXView`, otherwise a new encoding
- `x.toMutableXView()` — Converts any `X` implementation into a new `MutableXView`, never sharing the bytes of a view: a copy of them, otherwise a new encoding

`MutableXView`'s companion and its reading functions call `XView`'s, which costs nothing between value classes.
- `contentEquals(other)` — Compares view content (value classes can't override `equals`, so `==` compares array references)

### XLayout and XSlice

- `XLayout` — Internal object holding accessor functions for each field
- `XSlice` — A public class implementing `X` over a range of a larger array, with an internal constructor: you get one from `XView.wrapRange`, from the getter of a property of layout type `X`, as the item of a list of `X`, or as the receiver of `MutableXView.edit { }`. Its settable properties write in place and update the enclosing checksums; it has `toData()`, content-based `equals`/`hashCode`, and a `toString` naming it `XSlice`. Cast to it to write through a nested view, as the property's own type is the read-only interface
- Nested layouts whose encoding changes based on context (e.g., under `@ByteOrder(excludeStructure = true)`) get one layout/slice pair per distinct compiled form

### XFrame (for sealed layouts)

A `@JvmInline value class` over a `ByteArray` for sealed layout `X`. The `value` property is a view of the actual subtype, dispatched by discriminator or `@SizePolymorphic` size.

- `XFrame.encode(x)` — Creates a frame from any `X` subtype
- `XFrame.wrap(bytes, validateChecksum)` and `XFrame.wrapRange(bytes, from, to, validateChecksum)` — Wraps an array, dispatching to the correct subtype
- Does not implement `X` directly; the sealed hierarchy preserves exactly the subtypes you declared
- Has no setters of its own, so it has no mutable form: `value` is the subtype's `XSlice`, which writes in place when cast to it
- For a sealed layout with `@LengthPrefix`: `XFrame.valueStart(bytes, from)`, `XFrame.valueEnd(bytes, from)` and `XFrame.end(bytes, from)`, the bounds of the record at `from`, and `XFrame.writeRecord`, `XFrame.recordSize`, `XFrame.encodeRecord` and `XFrame.wrapValue` for a type and a value held apart, see [Type-length-value records](#type-length-value-records)

### Type-length-value records

A sealed layout with `@LengthPrefix` writes each subtype as a record: its `@SerializedByteValue` discriminator, its length, then the bytes the length counts, which hold the subtype's frame. A `@Fallback` subtype reads every discriminator the others do not have and is laid out after it like any other subtype; its `@Discriminator` property holds the discriminator, which `LayoutCompiler` makes a field of no bytes (`TypeLayout.Discriminator`) wherever it is declared. The [annotations](../annotations/) README has the rules and an example. DATA needs nothing new for them: the sealed DATA interface carries `@LengthPrefix`, the fallback's data class `@Fallback` and its property `@Discriminator`, which is all `BytesFormat` reads.

In a view:

- **A typed view of the bytes**: a record property, list item or `XFrame.value` is the subtype's slice over exactly the bytes its length counts, read in place. An `@Unsized` last property and a `@Trailing` tail end there, before any footer of the subtype's own, and bytes beyond what the subtype reads, which a later version may have appended, are skipped, as `BytesFormat` skips them.
- **Its type**: the class of the subtype, or, for a type no subtype has, the `@Fallback` subtype with the discriminator in its `@Discriminator` property. The discriminator is not in the bytes the slice covers, so the slice of a fallback, and only that slice, has one more field holding it, read from the frame when the frame dispatches to it; it is read-only, as writing it would change which subtype the record is.
- **Its bounds**: the end of a record is read from its length alone, so a list of records, `@Unsized` to the end of what holds it, steps over records of every type, known or not, without reading them. To walk records by hand, `XFrame.valueStart(bytes, from)` and `XFrame.valueEnd(bytes, from)` give the bytes a length counts and `XFrame.end(bytes, from)` where the next record starts; none of them allocates.
- **Writing**: a record's length is written once its subtype is, and one that does not fit the length throws, as in `BytesFormat`. A fallback writes its `@Discriminator` property to the discriminator, and throws when another subtype has it.
- **A type and a value held apart**: `XFrame.writeRecord(target, at, discriminator, value, from, to)` writes a record from a discriminator and value bytes kept apart, as a store keeps them, into an array the caller sized with `XFrame.recordSize(valueLength)`: the discriminator, the length in the frame's marking and byte order and the value as it is, framed by the layout's prefix, checksum and postfix, returning where it ends. It writes any discriminator, known or not, reads nothing of the value and allocates nothing; `XFrame.encodeRecord` returns a new array instead. `XFrame.wrapValue(discriminator, bytes, from, to)` views such a value in place: the same slice a list of these records gives for it, or the fallback's slice holding the discriminator for a type no subtype has, or a `NoSuchElementException` without a fallback. Like a list item it checks only its range. The discriminator is typed as the fallback's `@Discriminator` property, or without a fallback as the unsigned number of the discriminators' size in the layout's byte order.

A record's subtype is an ordinary layout within its bytes, so it composes with the rest of the vocabulary: its own flag header, flag-packed fields and numbers of several `@Size`s are laid out from the start of those bytes, and the generated code computes where everything after a number of several sizes starts from the size its flag bits record. Fields every subtype starts with, such as a time step, go in a plain interface each subtype extends, the fallback included:

```kotlin
interface Timed { @FlagIndex(0) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val step: Int }

@BytesLayout @LengthPrefix sealed interface Record {
    @BytesLayout @SerializedByteValue(0x20) interface Temperature : Record, Timed { val celsius: Short }
    @BytesLayout @Fallback interface Unknown : Record, Timed { @Discriminator val type: UByte; @Unsized val value: ByteArray }
}

// The frame of Record then has, among the rest:
fun recordSize(valueLength: Int): Int
fun writeRecord(target: ByteArray, at: Int, discriminator: UByte, value: ByteArray, from: Int = 0, to: Int = value.size): Int
fun encodeRecord(discriminator: UByte, value: ByteArray, from: Int = 0, to: Int = value.size): ByteArray
fun wrapValue(discriminator: UByte, bytes: ByteArray, from: Int = 0, to: Int = bytes.size): Record
```

The sealed layout itself still has no flag bits, cannot be `@SizePolymorphic`, and for VIEW its discriminators must all have the same size.

### Delta-coded lists

A `@DeltaCoded(step)` list is its first item, then a signed `step` from each item to the next, added modulo 2 to the power of an item's size; the [annotations](../annotations/#delta-coded-lists) README has the rules. Like `@Unsized` it runs to the end of what holds it, so it is the last property of a value or of a type-length-value record, whose length bounds it while further records follow; a property after it that is always written is a compile error saying so, and a view's `encode` throws `DataAfterUnconstrainedData` for one that may be absent, such as a nullable one, while it is present, as `BytesFormat` does. DATA needs nothing new: the data class carries `@DeltaCoded`, which `BytesFormat` reads.

In a view:

- **Its type**: a `List<Short>` reads as a `DeltaCodedShortList`, a `List<Int>` as a `DeltaCodedIntList` and a `List<Long>` as a `DeltaCodedLongList` of [view](../view/), the type the view and slice override the property with. A `ShortArray`, `IntArray` or `LongArray` property reads as a new array, as arrays do.
- **Reading**: `size` follows from the bytes alone, `(bytes - first item) / step + 1`. An item is only known from the one before it, so `get(i)` adds up the steps before it and takes time in proportion to `i`; read the items in order instead, with `forEachShort`, `forEachInt`, `forEachLong` or `copyInto(array, offset)`, none of which allocates or boxes, with `toShortArray()`, `toIntArray()` or `toLongArray()`, or with an iterator or a list iterator going forward, also over a `subList`, which boxes each item as any `List` does. Only the list object itself is allocated, once per read of the property.
- **Checking**: `wrap` throws `ByteArrayEndedBeforeSerializationCompleted`, as `BytesFormat` does, when the bytes are not a first item and whole steps; no bytes is an empty list.
- **Writing**: `encode` and the constructor-like `invoke` write the first item and the steps, and throw `IllegalArgumentException`, as `BytesFormat` does, for an item or a step that does not fit. The property has no setter, as its size depends on its items.

VIEW reads `Short`, `Int` and `Long` items; another item type, such as `Byte` or `UShort`, is reported as not yet supported for VIEW and works with DATA.

The annotation takes only what the encoding cannot get elsewhere. The first item is described the way the items of any list are, by its type, `@ItemSize`, `@ItemUnsigned` and `@ItemByteOrder`, and the end comes from the bound, as for `@Unsized`, so a step size is all that is left. Whether a list holds items or differences is not the encoder's choice: a protocol that sends either has a record type for each, one of them `@DeltaCoded`.

A second kind of delta frame is known and not supported yet: a reference value per channel, then blocks of a byte giving the bit width of its deltas and a byte giving its number of samples, followed by that many deltas per channel, sign-extended and packed least significant bit first, the channels interleaved and added without wrapping. `@DeltaCoded` grows into it without changing what today's parameter means:

- **Channels**: a `channels` parameter, 1 by default, makes the first `channels` items the references and each further item a step from the item `channels` before it, so the list holds the samples interleaved as they are sent.
- **Wrapping**: a `wrap` parameter, `true` by default, chooses between adding modulo the item size, as now, and plain addition, where an item that leaves the item's range does not decode and does not encode.
- **Blocks and bit packing**: a block header with a bit width and a count is a different end marking from a fixed `step`, so it becomes an annotation of its own, such as ``@DeltaBlocks(width = Length.`8_BIT`, count = Length.`8_BIT`)``, used instead of `@DeltaCoded` and taking `channels` and `wrap` too. `CollectionSettings.DeltaCoding` gains the channels, the wrapping and the block header, with steps counted in bits, read least significant bit first like flag-packed numbers.
- **Views**: the list API stays the same. The size of a blocked list is the sum of its block counts, found by stepping over the block headers once.

### Optional tails

A `@Trailing` property is present when the bytes of the value reach it; the [annotations](../annotations/#optional-tails) README has the rules and an example. DATA needs nothing new: the data class carries `@Trailing`, which `BytesFormat` reads.

In a view:

- **Reading**: the getter of a trailing property checks that where it starts lies before the end of the body and returns `null` when it does not, without reading anything past it; the getters before the tail are unchanged. The end of the body is the end of the array less the footer for an `XView`, `to` less the footer for `wrapRange`, the end of a record less its subtype's own footer for the subtype of a type-length-value record, and the end of the parent for a layout nested as its last property. A layout in the tail is bounded by the end of its parent too, so one the value ends inside of throws rather than reading what follows.
- **Checking**: the constructor checks nothing, so a view a Bluetooth client creates over a shorter value reads its tail as `null`. `wrap` and `validate` accept a value that only lacks trailing properties, and throw `ByteArrayEndedBeforeSerializationCompleted`, as `BytesFormat` does, for one that ends inside a trailing property or before the tail.
- **Writing**: `encode` and the constructor-like `invoke` write a `null` trailing property as nothing, and throw `TrailingAfterAbsent`, as `BytesFormat` does, for one that is not `null` after one that is. A trailing property has no setter, as a nullable property has none.
- **Allocation**: a getter allocates nothing for an absent property. A present number is returned as the nullable type the property declares, so it is boxed as any nullable number is: nothing for an `Int` in the `Integer` cache, a box of the number otherwise.

A tail in a frame accumulated `MOST_SIGNIFICANT_FIRST` is reported as not supported yet.

### <Prefix>BytesViews

One internal object per package, listing every layout generated with both DATA and VIEW, keyed by DATA serial name, prefixed like `<Prefix>BytesLayouts`.

## View behavior

### Validation and errors

- The view constructor checks nothing; call `wrap()` to validate before first read
- `XView.wrap()` checks what `BytesFormat` checks while decoding (prefixes, postfixes, checksums, lengths, discriminators, flag values) and throws the same exceptions
- `validate()` performs checks later if needed
- `isChecksumValid()` only reports checksum match status without throwing

### Writing

- `MutableXView` and `XSlice` write; `XView` only reads, and `toMutable()` copies it to write
- Setters update checksums of the frame and any enclosing frames automatically
- `MutableXView.edit { }` batches writes and updates checksums once at the end
- Properties whose values affect positions of later fields are read-only
- A property the interface declares `var` is a compile error for VIEW, as the read-only `XView` cannot override it, unless `@BytesLayout(readOnlySetters = ReadOnlySetters.THROW)` gives `XView` a setter that throws `UnsupportedOperationException`; a `var` that cannot be rewritten in place, being optional, of a varying size or gating other properties, is a compile error in both modes, as is a `var` `@Discriminator`
- Nested views are `XSlice` instances; cast to it to write a nested property in place, whether it came from an `XView` or a `MutableXView`

### Byte order

Layouts with `@ByteOrder` without `excludeStructure` are accumulated MOST_SIGNIFICANT_FIRST and read/written mirrored from the end of the array, matching `BytesFormat` behavior.

### Byte stuffing limitations

Byte stuffing cannot read in place. For byte-stuffed layouts:

- `XView.fromWire(wire)` and `MutableXView.fromWire(wire)` — Unstuff bytes into a new array and validate like `wrap()`
- `toWire()`, on both — Stuffs bytes again
- `XView.encodeToWire(value)` and `MutableXView.encodeToWire(value)` — Encode and stuff, as `BytesFormat` does
- Byte-stuffed string/collection properties are found by scanning for unescaped terminators; reading unstuffs a copy (no setters supported)
- Only the root frame is stuffed; byte stuffing on nested layouts and value classes (which `BytesFormat` supports), on sealed layouts, and on MOST_SIGNIFICANT_FIRST frames is not supported yet

## Supported VIEW features

Numbers (including multiple `@Size`s), flag and body Booleans, flag-packed and byte-valued enums, chars, strings of every end marking, lists/sets/maps/arrays with every count marking, delta-coded lists of `Short`, `Int` and `Long` items, sealed layouts and properties, type-length-value records and `@Fallback` subtypes, nullable, `@PresentWhenAllSet` and `@Trailing` properties, `@Reserved`, nested layouts and value classes, prefixes, postfixes, and checksums.

Positions depending on earlier properties' presence or size are computed from the flag header at read time; others are constants. String getters decode on every read; only fixed-length strings have setters. A `@Count` collection of fixed-size items has a setter that rewrites its items in place and, like the constructors, refuses another number of items. Lists are read in place: `FixedStrideList` when all items are the same size, a `DeltaCodedShortList` or `DeltaCodedIntList` when `@DeltaCoded`, otherwise `ScannedList`. Sets, maps and arrays are decoded when read.

## Unsupported VIEW features

Configurations using unsupported features generate a KSP error "VIEW not yet supported for ...", or a warning (skipping the view) if `skipUnsupportedViews = true`.

## Layout constraints

- Invalid layouts (ones `BytesFormat` would reject at first encoding) are compile errors. Data after data of an unconstrained size, such as an `@Unsized` list, is one when it is written for every value; when the data before or after may be absent, a view's `encode` and constructor-like `invoke` throw `DataAfterUnconstrainedData` for the values `BytesFormat` throws it for. Bytes `@Reserved` after such data count as data after it, and so does an item of a collection after an item that ends in such data
- Layouts cannot be generic; declare a concrete sub-interface instead
- A sealed super-interface of a layout must itself be a `@BytesLayout`: extending a sealed interface is how a layout becomes a subtype that its parent dispatches by discriminator or size, so a sealed parent without the annotation is most likely one where it was forgotten, and generating the layout on its own would encode it without its discriminator. To only group layouts, or to share properties between them, extend an interface that is not sealed
- Properties cannot declare default values, and none can be named `Companion`
- A layout generating VIEW cannot have a property named `bytes`, which would clash with the view's public `bytes` property holding its array, nor `sliceBytes`, `sliceStart`, `sliceLimit` or `sliceParent`, the slice's own properties. This is reported like an unsupported feature, so `skipUnsupportedViews` skips the view instead
- A layout interface cannot carry a `@SerialName`: its serial name is its qualified name, which sealed dispatch and the layout dump parity rely on
- Nested layouts flatten into one generated name (`Outer.Inner` generates `OuterInnerData`), and a writing view prefixes it with `Mutable` (`Foo` generates `MutableFooView`, as does a `MutableFoo` or `Mutable.Foo` that generates VIEW), so two layouts generating a class of the same name are both reported
- Type aliases are expanded as kotlinx sees them, nullability and type arguments included
- A `@Serializable` value class wrapping a layout must name the layout's serializer on its property, `@Serializable(with = XData.InterfaceSerializer::class)`; kotlinx otherwise serializes the interface polymorphically
- `Int24`, `UInt24`, `Int40`, `UInt40`, `Int48`, `UInt48`, `MedFloat16` and `MedFloat32` have no kotlinx serializer, so only a VIEW-only layout can hold them
- An explicit `@BytesLayout(generate = [...])` overrides the module-wide default, even when it lists what the annotation's own default does

## Source sets and modules

Each KSP pass indexes every `@BytesLayout` it sees, so a layout in `jvmMain` can use one in `commonMain`; the pass generates only its own source set's layouts, the common ones coming from the metadata pass. VIEW does not support nesting such a layout in a way that changes its encoding, which would need a context the common pass did not generate. KSP does not process test or other intermediate source sets, so `@BytesLayout` interfaces there generate nothing.

`@BytesLayout` is kept in compiled code, so a layout can also hold a layout of a module it depends on, `Y`. The processor reads `Y`'s properties and their annotations from the compiled interface, in declaration order, and lays it out like a layout of its own module:

- DATA refers to the `YData.InterfaceSerializer` the other module generated, so a DATA layout can only hold `Y` when that module generated DATA for it
- VIEW cannot use the other module's `YLayout`, which is internal to it, nor construct its `YSlice`, so each layout `X` holding `Y` gets accessors of its own: `XLayout` nests a `YLayout` object and a `YSlice` class for every context `X` uses `Y` in, whatever the other module generated. Code of the holding module casts to `XLayout.YSlice` to write through it; to other modules it is a read-only `Y`
- A sealed `Y` works the same way, its subtypes nested next to it
- Any other interface of another module is open polymorphism, which generated layouts do not support

When a layout uses a type another processor generates in a later round, every layout waits for that round, so each is processed with the whole index.
