# Base i18n
| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides localization primitives for Kaluga: locales, unit systems and locale-aware String casing.

## Installing
This library is available on Maven Central. You can import Kaluga Base i18n as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:i18n:$kalugaVersion")
}
```

## Locale
`KalugaLocale` represents a specific geographical, political, or cultural region. Create one from language, country and variant codes using `KalugaLocale.createLocale(...)`, or use `KalugaLocale.defaultLocale` for the current default. The complete list of locales supported by the platform is available through `KalugaLocale.availableLocales`.

A `KalugaLocale` exposes its `countryCode`, `languageCode`, `scriptCode` and `variantCode`, localized display names, quotation marks, and its `unitSystem`.

## Unit System
`UnitSystem` describes whether a locale uses the `METRIC`, `IMPERIAL` or `MIXED` measurement system. For more advanced functionality use [Kaluga Scientific](../../scientific/).

## String Casing
Convert a `String` to lower or upper case in a locale-aware manner using `lowerCased(locale)` and `upperCased(locale)`.
