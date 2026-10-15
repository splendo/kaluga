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

### XView

A `@JvmInline value class` over a `ByteArray` implementing `X`. Properties read and write directly to the byte array in place.

- `XView.encode(x)` — Creates a view from an `X` implementation, sized automatically
- `XView(field1, field2, …)` — Creates a view from individual field values, one parameter per property
- `XView.wrap(bytes, validateChecksum)` — Wraps an existing array, optionally validating prefixes, postfixes, checksums, lengths
- `XView.wrapRange(bytes, from, to, validateChecksum)` — Views a slice of a larger array
- `toData()` — Copies the view's bytes into an `XData` instance
- `x.toXView()` — Converts any `X` implementation into a view
- `contentEquals(other)` — Compares view content (value classes can't override `equals`, so `==` compares array references)

### XLayout and XSlice

- `XLayout` — Internal object holding accessor functions for each field
- `XSlice` — A public class implementing `X` over a range of a larger array, with an internal constructor: you get one from `XView.wrapRange`, from the getter of a property of layout type `X`, as the item of a list of `X`, or as the receiver of `edit { }`. Its settable properties write in place and update the enclosing checksums; it has `toData()`, content-based `equals`/`hashCode`, and a `toString` naming it `XSlice`. Cast to it to write through a nested view, as the property's own type is the read-only interface
- Nested layouts whose encoding changes based on context (e.g., under `@ByteOrder(excludeStructure = true)`) get one layout/slice pair per distinct compiled form

### XFrame (for sealed layouts)

A `@JvmInline value class` over a `ByteArray` for sealed layout `X`. The `value` property is a view of the actual subtype, dispatched by discriminator or `@SizePolymorphic` size.

- `XFrame.encode(x)` — Creates a frame from any `X` subtype
- `XFrame.wrap(bytes, validateChecksum)` and `XFrame.wrapRange(bytes, from, to, validateChecksum)` — Wraps an array, dispatching to the correct subtype
- Does not implement `X` directly; the sealed hierarchy preserves exactly the subtypes you declared

### <Prefix>BytesViews

One internal object per package, listing every layout generated with both DATA and VIEW, keyed by DATA serial name, prefixed like `<Prefix>BytesLayouts`.

## View behavior

### Validation and errors

- The view constructor checks nothing; call `wrap()` to validate before first read
- `XView.wrap()` checks what `BytesFormat` checks while decoding (prefixes, postfixes, checksums, lengths, discriminators, flag values) and throws the same exceptions
- `validate()` performs checks later if needed
- `isChecksumValid()` only reports checksum match status without throwing

### Writing

- Setters update checksums of the frame and any enclosing frames automatically
- `edit { }` batches writes and updates checksums once at the end
- Properties whose values affect positions of later fields are read-only
- Nested views are `XSlice` instances; cast to it to write a nested property in place

### Byte order

Layouts with `@ByteOrder` without `excludeStructure` are accumulated MOST_SIGNIFICANT_FIRST and read/written mirrored from the end of the array, matching `BytesFormat` behavior.

### Byte stuffing limitations

Byte stuffing cannot read in place. For byte-stuffed layouts:

- `XView.fromWire(wire)` — Unstuffs bytes and validates like `wrap()`
- `toWire()` — Stuffs bytes again
- `XView.encodeToWire(value)` — Encodes via `BytesFormat` (which does the stuffing)
- Byte-stuffed string/collection properties are found by scanning for unescaped terminators; reading unstuffs a copy (no setters supported)
- Only the root frame is stuffed; byte stuffing on nested layouts and value classes (which `BytesFormat` supports), on sealed layouts, and on MOST_SIGNIFICANT_FIRST frames is not supported yet

## Supported VIEW features

Numbers (including multiple `@Size`s), flag and body Booleans, flag-packed and byte-valued enums, chars, strings of every end marking, lists/sets/maps/arrays with every count marking, sealed layouts and properties, nullable and `@PresentWhenAllSet` properties, `@Reserved`, nested layouts and value classes, prefixes, postfixes, and checksums.

Positions depending on earlier properties' presence or size are computed from the flag header at read time; others are constants. String getters decode on every read; only fixed-length strings have setters. Lists are read in place: `FixedStrideList` when all items are the same size, otherwise `ScannedList`. Sets, maps and arrays are decoded when read.

## Unsupported VIEW features

Configurations using unsupported features generate a KSP error "VIEW not yet supported for ...", or a warning (skipping the view) if `skipUnsupportedViews = true`.

## Layout constraints

- Invalid layouts (ones `BytesFormat` would reject at first encoding) are compile errors
- Layouts cannot be generic; declare a concrete sub-interface instead
- Properties cannot declare default values, and none can be named `Companion`
- A layout interface cannot carry a `@SerialName`: its serial name is its qualified name, which sealed dispatch and the layout dump parity rely on
- Nested layouts flatten into one generated name (`Outer.Inner` generates `OuterInnerData`), so two layouts that flatten to the same name are both reported
- Type aliases are expanded as kotlinx sees them, nullability and type arguments included
- A `@Serializable` value class wrapping a layout must name the layout's serializer on its property, `@Serializable(with = XData.InterfaceSerializer::class)`; kotlinx otherwise serializes the interface polymorphically
- `Int24`, `UInt24`, `Int40`, `UInt40`, `Int48`, `UInt48`, `MedFloat16` and `MedFloat32` have no kotlinx serializer, so only a VIEW-only layout can hold them
- An explicit `@BytesLayout(generate = [...])` overrides the module-wide default, even when it lists what the annotation's own default does

## Source sets and modules

Each KSP pass indexes every `@BytesLayout` it sees, so a layout in `jvmMain` can use one in `commonMain`; the pass generates only its own source set's layouts, the common ones coming from the metadata pass. VIEW does not support nesting such a layout in a way that changes its encoding, which would need a context the common pass did not generate. `@BytesLayout` is not retained in compiled code, so layouts from another module are not supported yet and are reported as such. KSP does not process test or other intermediate source sets, so `@BytesLayout` interfaces there generate nothing.

When a layout uses a type another processor generates in a later round, every layout waits for that round, so each is processed with the whole index.
