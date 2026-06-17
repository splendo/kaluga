## Formatting

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library formats and parses numbers and strings in a locale-aware manner.

## Installing
This library is available on Maven Central. You can import Kaluga Formatting as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:formatting:$kalugaVersion")
}
```

## NumberFormatter
A `Number` can be formatted and parsed using a `NumberFormatter`. The formatter takes a `KalugaLocale` and a `NumberFormatStyle` (such as `Integer`, `Decimal`, `Percentage`, `Permillage`, `Scientific`, `Currency` or a custom `Pattern`), and exposes `format(number)` and `parse(string)`.

## StringFormatter
A `String` can be formatted to include different data types using `StringFormatter`, which implements printf-style formatting. The `String.format(vararg args, locale)` extension is a convenient shorthand. Custom types can participate in formatting by implementing the formatting interface.
