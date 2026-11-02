# Bytes code generation plugin

`com.splendo.kaluga.bytes.plugin` is a Gradle plugin that generates `BytesFormat` data classes and/or zero-copy views
from a declarative, annotated description of a byte-packed wire layout. You describe the layout as a plain interface
and annotate it with `@BytesLayout`; the plugin generates the requested implementations on top of
[serialization](../serialization/) and/or [view](../view/).

It is built from three pieces:

| Module | Usage | Artifact |
|---|---|---|
| [annotations](../annotations/) | The `@BytesLayout` annotation used to describe a layout | `com.splendo.kaluga.bytes:annotations` |
| [ksp](../ksp/) | The KSP processor that generates the code | `com.splendo.kaluga.bytes:ksp` |
| [plugin](plugin/) | The Gradle plugin wiring KSP, the processor and the runtime dependencies together | `com.splendo.kaluga.bytes.plugin` |

## What gets generated

The processor generates DATA classes (`@Serializable` data classes) and, unless the layout uses a feature VIEW generation doesn't support yet, zero-copy views (`@JvmInline value class` over `ByteArray`) — views handle variable-position fields too, computing their position from the flag header or preceding fields at read time. See [ksp](../ksp/) for details of what each implementation type contains and which features VIEW generation doesn't support.

For a layout interface `X`:

- **DATA:** `XData` (a `@Serializable` data class), `XData.LAYOUT_DUMP`, `XData.from()`, and `XData.InterfaceSerializer`
- **VIEW:** `XView` (a `@JvmInline value class`), `XLayout`, and for sealed roots `XFrame`

The generated code produces exactly the same bytes as `BytesFormat` for the same values on every platform. See [`example/validation/format`](example/validation/format) for the `BytesFormat` test suite running on the generated DATA classes and views, and [`example/validation/core`](example/validation/core) for a minimal setup. Both run on the Android host, iOS, JS (Node and browser) and wasmJs; wasmJs browser tests need a headless Chrome.

## Applying the plugin

```kotlin
plugins {
    id("com.splendo.kaluga.bytes.plugin")
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()
    // macosArm64(), etc.
}
```

The plugin applies the Kotlin Multiplatform, KSP and Kotlin serialization (`org.jetbrains.kotlin.plugin.serialization`,
unless it is already applied) plugins, and adds `bytes:annotations`, `bytes:serialization` and `bytes:view` to
`commonMain`. Any interface can override the module-wide `generate`, so what a module generates is only known once KSP
has read them all; that is why both runtimes and the serialization plugin are always there. Each KSP pass gets a
`registryPrefix` from the project path and its source set, so the generated registry objects of different modules and
source sets never clash.

## Configuring generation

```kotlin
import com.splendo.kaluga.bytes.plugin.Generate

bytes {
    generate.set(setOf(Generate.DATA)) // module-wide default; an interface's own @BytesLayout(generate = [...]) overrides it
    skipUnsupportedViews.set(true)
}
```

| Option | Default | Description |
|---|---|---|
| `generate` | `[DATA, VIEW]` | Module-wide default for which implementations to generate from a `@BytesLayout` interface without its own `generate` override. Must not be empty. |
| `skipUnsupportedViews` | `false` | When `true`, a layout using a feature VIEW generation does not support yet gets no view and a build warning; otherwise it fails the build. |

## Example

Here is a complete example of a layout that encodes heart rate measurements:

```kotlin
import com.splendo.kaluga.bytes.annotations.*

@BytesLayout
interface HeartRate {
    @Size(Length.`8_BIT`)
    @Unsigned
    val bpm: Int
}
```

The plugin generates:

```kotlin
// Generated DATA class (can be used with BytesFormat)
@Serializable
data class HeartRateData(
    @Size(Length.`8_BIT`)
    @Unsigned
    val bpm: Int,
) : HeartRate

// Generated VIEW (zero-copy, @JvmInline value class)
@JvmInline
value class HeartRateView(val bytes: ByteArray) : HeartRate {
    override val bpm: Int
        get() = /* read directly from bytes */
}

// Usage
val data = HeartRateData(bpm = 72)

// Via BytesFormat (the default format)
val encoded = BytesFormat.encodeToByteArray(HeartRateData.serializer(), data)
val decoded = BytesFormat.decodeFromByteArray(HeartRateData.serializer(), encoded)

// Via VIEW (zero-copy, no allocations)
val view = HeartRateView.encode(data)
val sameBytes = view.bytes
val viewDecoded = HeartRateView.wrap(encoded)
```

For a more complex example involving enums, sealed classes and nested structures, see [`example/validation/format/src/commonMain/kotlin/EnumsPolymorphismFixtures.kt`](example/validation/format/src/commonMain/kotlin/EnumsPolymorphismFixtures.kt).

## SerialName note for sealed/enum class discriminators

When using sealed classes or enums without an explicit `@SerializedByteValue`, the discriminator is the interface's fully qualified name encoded as UTF-8. To use a class-name-based discriminator without restating the serialName, rename the interface to match, or use `@SerializedByteValue` to specify the byte sequence explicitly.
