# Migrating from Kaluga 1.6.0 to 2.0.0

Kaluga 2.0.0 reorganises every module into **AndroidX-style nested groups**. Instead of one flat
`com.splendo.kaluga` groupId for everything, each feature now has its own groupId
(`com.splendo.kaluga.<feature>`) and short artifact ids. A handful of standalone utilities keep the
flat groupId.

This is a **breaking change to Maven coordinates** (and to a few packages and APIs), but the runtime
behaviour of the modules is otherwise unchanged. For most projects, migrating is a find-and-replace of
your dependency coordinates plus a couple of targeted fixes described below.

> **Tip:** the change is purely in *coordinates/packages*. If your build compiled against 1.6.0, it
> will compile against 2.0.0 once the coordinates (and the test-utils imports) are updated.

---

## 1. Bump the version

```diff
- kaluga = "1.6.0"
+ kaluga = "2.0.0"
```

## 2. (Optional) Use the published version catalog

2.0.0 publishes a Gradle **version catalog** that already contains every module under stable aliases.
This is the lowest-effort way to migrate — you reference modules by alias and never hand-write a
coordinate:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        create("kaluga") {
            from("com.splendo.kaluga:catalog:2.0.0")
        }
    }
}
```

```kotlin
// build.gradle.kts
dependencies {
    implementation(kaluga.bluetooth.client)
    implementation(kaluga.permissions.location)
    testImplementation(kaluga.architecture.testutils)
    // ...
}
```

The alias → coordinate mapping is the one in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).
Aliases are feature-first with the qualifier last, e.g. `kaluga-bluetooth-client` → `kaluga.bluetooth.client`,
`kaluga-architecture-testutils` → `kaluga.architecture.testutils`, `kaluga-lifecycle-compose` → `kaluga.lifecycle.compose`.

---

## 3. Coordinate changes

The general rule:

| 1.6.0 | 2.0.0 |
|-------|-------|
| `com.splendo.kaluga:<feature>` | `com.splendo.kaluga.<feature>:<feature>` |
| `com.splendo.kaluga:<feature>-compose` | `com.splendo.kaluga.<feature>:compose` |
| `com.splendo.kaluga:<feature>-databinding` | `com.splendo.kaluga.<feature>:databinding` |
| `com.splendo.kaluga:<x>-permissions` | `com.splendo.kaluga.permissions:<x>` |
| `com.splendo.kaluga:test-utils-<feature>` | `com.splendo.kaluga.<feature>:test` |

A few utilities **keep the flat groupId** (no change): `com.splendo.kaluga:links`,
`com.splendo.kaluga:logging`, `com.splendo.kaluga:review`.

### Full mapping

| 1.6.0 coordinate | 2.0.0 coordinate |
|------------------|------------------|
| `com.splendo.kaluga:base` | **split** — see [Base and date-time split](#base-and-date-time-split) (`…base:core` + `state` / `decimal` / `bytes` / `crc` / `i18n` / `formatting`) |
| `com.splendo.kaluga:alerts` | `com.splendo.kaluga.alerts:alerts` |
| `com.splendo.kaluga:architecture` | `com.splendo.kaluga.architecture:architecture` |
| `com.splendo.kaluga:architecture-compose` | `com.splendo.kaluga.architecture:compose` |
| `com.splendo.kaluga:beacons` | `com.splendo.kaluga.bluetooth:beacons` |
| `com.splendo.kaluga:bluetooth` | **removed** — see [Bluetooth](#bluetooth) (use `…bluetooth:client` / `…bluetooth:server`) |
| `com.splendo.kaluga:date-time` | **split** — see [Base and date-time split](#base-and-date-time-split) (`com.splendo.kaluga.date-time:date-time` + `…date-time:timer`) |
| `com.splendo.kaluga:date-time-picker` | `com.splendo.kaluga.date-time-picker:date-time-picker` |
| `com.splendo.kaluga:hud` | `com.splendo.kaluga.hud:hud` |
| `com.splendo.kaluga:keyboard` | `com.splendo.kaluga.keyboard:keyboard` |
| `com.splendo.kaluga:keyboard-compose` | `com.splendo.kaluga.keyboard:compose` |
| `com.splendo.kaluga:links` | `com.splendo.kaluga:links` *(unchanged)* |
| `com.splendo.kaluga:location` | `com.splendo.kaluga.location:location` |
| `com.splendo.kaluga:logging` | `com.splendo.kaluga:logging` *(unchanged)* |
| `com.splendo.kaluga:media` | `com.splendo.kaluga.media:media` |
| `com.splendo.kaluga:resources` | `com.splendo.kaluga.resources:resources` |
| `com.splendo.kaluga:resources-compose` | `com.splendo.kaluga.resources:compose` |
| `com.splendo.kaluga:review` | `com.splendo.kaluga:review` *(unchanged)* |
| `com.splendo.kaluga:scientific` | `com.splendo.kaluga.scientific:scientific` |
| `com.splendo.kaluga:scientific-converters` | `com.splendo.kaluga.scientific:converters` |
| `com.splendo.kaluga:service` | `com.splendo.kaluga.service:service` |
| `com.splendo.kaluga:system` | `com.splendo.kaluga.system:system` |
| `com.splendo.kaluga:base-permissions` | `com.splendo.kaluga.permissions:core` |
| `com.splendo.kaluga:bluetooth-permissions` | `com.splendo.kaluga.permissions:bluetooth` |
| `com.splendo.kaluga:calendar-permissions` | `com.splendo.kaluga.permissions:calendar` |
| `com.splendo.kaluga:camera-permissions` | `com.splendo.kaluga.permissions:camera` |
| `com.splendo.kaluga:contacts-permissions` | `com.splendo.kaluga.permissions:contacts` |
| `com.splendo.kaluga:location-permissions` | `com.splendo.kaluga.permissions:location` |
| `com.splendo.kaluga:microphone-permissions` | `com.splendo.kaluga.permissions:microphone` |
| `com.splendo.kaluga:notifications-permissions` | `com.splendo.kaluga.permissions:notifications` |
| `com.splendo.kaluga:storage-permissions` | `com.splendo.kaluga.permissions:storage` |
| `com.splendo.kaluga:test-utils-base` | `com.splendo.kaluga.base:test` |
| `com.splendo.kaluga:test-utils-alerts` | `com.splendo.kaluga.alerts:test` |
| `com.splendo.kaluga:test-utils-architecture` | `com.splendo.kaluga.architecture:test` |
| `com.splendo.kaluga:test-utils-koin` | `com.splendo.kaluga.architecture:test-koin` |
| `com.splendo.kaluga:test-utils-bluetooth` | **removed** — split into `…bluetooth:test-core` / `…bluetooth:test-client` / `…bluetooth:test-server` |
| `com.splendo.kaluga:test-utils-date-time-picker` | `com.splendo.kaluga.date-time-picker:test` |
| `com.splendo.kaluga:test-utils-hud` | `com.splendo.kaluga.hud:test` |
| `com.splendo.kaluga:test-utils-keyboard` | `com.splendo.kaluga.keyboard:test` |
| `com.splendo.kaluga:test-utils-location` | `com.splendo.kaluga.location:test` |
| `com.splendo.kaluga:test-utils-media` | `com.splendo.kaluga.media:test` |
| `com.splendo.kaluga:test-utils-permissions` | `com.splendo.kaluga.permissions:test` |
| `com.splendo.kaluga:test-utils-resources` | `com.splendo.kaluga.resources:test` |
| `com.splendo.kaluga:test-utils-service` | `com.splendo.kaluga.service:test` |
| `com.splendo.kaluga:test-utils-system` | `com.splendo.kaluga.system:test` |

### New modules in 2.0.0

These were not separately published in 1.6.0 (the Lifecycle module was previously part of `architecture` — see [below](#lifecycle-extracted-from-architecture)):

| Module | Coordinate |
|--------|------------|
| Lifecycle | `com.splendo.kaluga.lifecycle:lifecycle` |
| Lifecycle (Compose) | `com.splendo.kaluga.lifecycle:compose` |
| Lifecycle (test-utils) | `com.splendo.kaluga.lifecycle:test` |
| Media (Compose) | `com.splendo.kaluga.media:compose` |
| Resources (Data Binding) | `com.splendo.kaluga.resources:databinding` |

---

## 4. Breaking changes that need code edits

### Base and date-time split

The single `base` module and the single `date-time` module have each been split into
focused modules, and several types moved to a package that matches their new module. Add a
dependency on the specific module(s) you use, and update the affected imports.

`base` → `base:core` plus `base:state`, `base:decimal`, `base:bytes`, `base:crc`, `base:i18n`,
`base:formatting`. `date-time` → `date-time:date-time` (the calendar types and date formatter)
and `date-time:timer` (`RecurringTimer`).

The following types changed package (find-and-replace the import):

| Type(s) | Old package (1.6.0 / earlier 2.0.0) | New package | Module |
|---------|-------------------------------------|-------------|--------|
| `KalugaDate`, `DefaultKalugaDate`, `KalugaTimeZone`, `KalugaDateFormatter` (+ the `KalugaDate` extensions) | `com.splendo.kaluga.base.utils` / `…base.text` | `com.splendo.kaluga.datetime` | `date-time:date-time` |
| `KalugaLocale`, `UnitSystem`, `AvailableLocales`, `String.lowerCased`/`upperCased` | `com.splendo.kaluga.base.utils` / `…base.text` | `com.splendo.kaluga.base.i18n` | `base:i18n` |
| `NumberFormatter`, `StringFormatter`, `FormatSpecifier`, `lineSeparator`, … | `com.splendo.kaluga.base.text` | `com.splendo.kaluga.base.formatting` | `base:formatting` |
| `Decimal`, `toDecimal`, `RoundingMode` (the `Decimal` one) | `com.splendo.kaluga.base.utils` | `com.splendo.kaluga.base.decimal` | `base:decimal` |

Types whose package is **unchanged** but which now live in a smaller module (so you only need to
add the module dependency): `KalugaState`/`StateRepo` (`com.splendo.kaluga.base.state` → `base:state`),
the byte utilities (`com.splendo.kaluga.base.bytes` → `base:bytes`), and CRC
(`com.splendo.kaluga.base.crc` → `base:crc`). The Luxon JS interop moved from
`com.splendo.kaluga.base.externals` to `com.splendo.kaluga.datetime.externals`.

### Bluetooth

The aggregate `com.splendo.kaluga:bluetooth` module (and the `Bluetooth` / `BluetoothBuilder` facade
it provided) has been **removed**. Bluetooth is now three modules sharing a common core:

- `com.splendo.kaluga.bluetooth:core` — shared attributes and the BluetoothFormat (de)serialization framework
- `com.splendo.kaluga.bluetooth:client` — scanning for / connecting to BLE devices (depends on `core`)
- `com.splendo.kaluga.bluetooth:server` — advertising / exposing GATT attributes (depends on `core`)

Add `client` and/or `server` (each pulls in `core` transitively) and obtain instances from the
concrete builders in those modules — `BluetoothClientBuilder` / `BluetoothServerBuilder` — instead of
the removed `BluetoothBuilder` facade.

`beacons` now lives under the bluetooth group: `com.splendo.kaluga.bluetooth:beacons`.

### Permissions

- The permissions base module was renamed **`base` → `core`**: `com.splendo.kaluga:base-permissions`
  is now `com.splendo.kaluga.permissions:core`. (The Kotlin package is unchanged — only the coordinate
  moved.)
- The `registerAllPermissions()` facade has been **removed**. Register only the permission modules you
  actually use against your `PermissionsBuilder`, declaring each per-permission module
  (`com.splendo.kaluga.permissions:location`, `:camera`, …) as a dependency.

### Lifecycle extracted from Architecture

The lifecycle-host binding types — `LifecycleSubscribable` and friends, used to bind a service/UI
builder to an Android `Activity`, an iOS `UIViewController` or a macOS `NSWindow` — have moved **out of
`architecture` into the new `lifecycle` module** (`com.splendo.kaluga.lifecycle:lifecycle`), and their
package changed:

```
com.splendo.kaluga.architecture.lifecycle.*   →   com.splendo.kaluga.lifecycle.*
```

Consequently, the feature modules that bind builders to a platform host — **alerts**, **hud**,
**media**, … — now depend on `com.splendo.kaluga.lifecycle:lifecycle` instead of `architecture`. That
dependency is exposed transitively (`api`), so updating those modules' coordinates is usually enough;
but if your **own** code imported the lifecycle types from `com.splendo.kaluga.architecture.lifecycle`,
re-point the imports to `com.splendo.kaluga.lifecycle`.

> `BaseLifecycleViewModel` / `LifecycleViewModel` stay in `architecture`; only the lifecycle-*binding*
> types moved.

### Media now binds a surface explicitly

In 1.6.0 the media player received its video surface from a `MediaSurfaceProvider`, which was a
`LifecycleSubscribable` — the surface was delivered through a lifecycle subscription. In 2.0 the media
module is **no longer lifecycle-based**; the view that hosts the surface **binds it explicitly** via a
`MediaSurfaceBinder`:

- `MediaSurfaceProvider` (and its `LifecycleSubscribable` base) has been **removed**.
- `BaseMediaManager` (and `MediaManager.Builder.create`) now take a `MediaSurfaceBinder?` in place of
  the old `MediaSurfaceProvider?`.
- The hosting view binds/detaches the surface with `MediaSurfaceBinder.bind(surface)` /
  `MediaSurfaceBinder.unbind()`; the bound surface is still forwarded automatically to the player's
  `renderVideoOnSurface`.
- The new `com.splendo.kaluga.media:compose` module provides Compose helpers for performing this
  binding.

### Test-utils package rename

In addition to the coordinate change, the **Kotlin packages** of the test-utils modules were flipped
to match the rest of each feature:

```
com.splendo.kaluga.test.<feature>   →   com.splendo.kaluga.<feature>.test
```

Update your test imports accordingly, for example:

| 1.6.0 import | 2.0.0 import |
|--------------|--------------|
| `com.splendo.kaluga.test.base.*` | `com.splendo.kaluga.base.test.*` |
| `com.splendo.kaluga.test.base.mock.*` | `com.splendo.kaluga.base.test.mock.*` |
| `com.splendo.kaluga.test.architecture.*` | `com.splendo.kaluga.architecture.test.*` |
| `com.splendo.kaluga.test.bluetooth.*` | `com.splendo.kaluga.bluetooth.test.*` |
| `com.splendo.kaluga.test.permissions.*` | `com.splendo.kaluga.permissions.test.*` |
| `com.splendo.kaluga.test.koin.*` | `com.splendo.kaluga.koin.test.*` |

A regex replace of `com\.splendo\.kaluga\.test\.(<feature>)` → `com.splendo.kaluga.$1.test` across your
test sources covers it.

> Only the **test-utils** packages moved. Main module packages (e.g.
> `com.splendo.kaluga.permissions.base`, `com.splendo.kaluga.bluetooth`) are **unchanged** — updating
> the dependency coordinate is enough for non-test code.

### Removed umbrella test-utils

The umbrella `com.splendo.kaluga:test-utils` aggregate has been removed. Depend on the specific
`com.splendo.kaluga.<feature>:test` modules you need.

---

## 5. Suggested migration steps

1. Set `kaluga = "2.0.0"`.
2. Replace your dependency coordinates using the [table above](#full-mapping) (or switch to the
   published version catalog — section 2).
3. Replace the `com.splendo.kaluga:bluetooth` dependency with `…bluetooth:client` / `…bluetooth:server`
   and adopt the concrete builders.
4. Replace `registerAllPermissions()` with explicit per-permission registration.
5. Update test imports for the `com.splendo.kaluga.test.<feature>` → `com.splendo.kaluga.<feature>.test`
   package flip.
6. Build, and fix any remaining unresolved references.
