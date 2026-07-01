# Date Time

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library provides multiplatform classes to work with dates, time zones and date formatting.

## Installing
This library is available on Maven Central. You can import Kaluga Date Time as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.date-time:date-time:$kalugaVersion")
}
```

## Date
Kaluga includes a `KalugaDate` class to manage and compare time.
Dates can be created using either `DefaultKalugaDate.now()` or `DefaultKalugaDate.epoch()`.
Dates are mutable by default and can be compared.

```kotlin
val today = DefaultKalugaDate.now()
val tomorrow = today.copy().apply {
    day += 1
}
assertTrue(today < tomorrow)
```

Convenience accessors such as `today()`, `tomorrow()`, `isToday`, `isOnSameDay(...)`, `toStartOfDay()` and the `plus`/`minus` operators are also available.

## Time Zone
`KalugaTimeZone` represents a time zone. Obtain one with `KalugaTimeZone.get(identifier)` or `KalugaTimeZone.current()`, list the available identifiers through `KalugaTimeZone.availableIdentifiers`, and use `KalugaTimeZone.utc` for UTC. A time zone exposes its `identifier`, localized `displayName(...)` and GMT offsets.

## Formatting
A `KalugaDate` can be formatted and parsed using a `KalugaDateFormatter`.
Create one for a date and/or time style with `KalugaDateFormatter.dateFormat(...)`, `KalugaDateFormatter.timeFormat(...)` or `KalugaDateFormatter.dateTimeFormat(...)`, or with an explicit pattern using `KalugaDateFormatter.patternFormat(pattern, ...)`. The formatter exposes `format(date)` and `parse(string)`.
