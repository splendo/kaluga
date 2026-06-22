# Base Decimal

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides a `Decimal` type for high-precision decimal arithmetic.

## Installing
This library is available on Maven Central. You can import Kaluga Base Decimal as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.base:decimal:$kalugaVersion")
}
```

## Decimal

Kaluga includes a `Decimal` class to manage decimal numbers with high precision.
Decimal can be created from any `Number` using `toDecimal()` or back to a `Double`/`Int` using `toDouble()`/`toInt()` respectively.

Use Decimals to do standard arithmetic operations. A Rounding mode or scale can be provided for these calculations.
