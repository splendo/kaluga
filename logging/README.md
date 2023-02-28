## Logging

This module provided simplified access to logging capabilities of [Kotlin Napier Log](https://github.com/AAkira/Napier).

## Installing
This library is available on Maven Central. You can import Kaluga Logging as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:logging:$kalugaVersion")
}
```

## Usage
Logging is done using a `Logger`. By default Kaluga provides an application wide logger via `com.splendo.kaluga.logging.logger`. When logging directly, this `Logger` is used.
Create a custom `Logger` to get more control over what logging occurs.

This library includes a `defaultLogger`, `NappierLogger`, and a `RestrictedLogger`. The latter supports restricting logging to specific `LogLevel`.

### Logging

Here's a list of functions available for usage. These can be called either directly or on on a `Logger`. When called directly the platform logger will be used. 

* `debug(message)` \\ `debug(tag, message)` \\ `debug(exception)` \\ `debug(tag, exception)` \\ `debug(tag, () -> String)`
* `info(message)` \\ `info(tag, message)` \\ `info(exception)` \\ `info(tag, exception)` \\ `info(tag, () -> String)`
* `error(message)` \\ `error(tag, message)` \\ `error(exception)` \\ `error(tag, exception)` \\ `error(tag, () -> String)`
* `warn(message)` \\ `warn(tag, message)` \\ `warn(exception)` \\ `warn(tag, exception)` \\ `warn(tag, () -> String)`
* `log(level, message)` \\ `log(level, tag, message)` \\`log(level, tag, () -> String)`

#### Message or `()->String`?

In case of string template usage consider using `() -> String`. If logging call will be filtered out, then function providing message will not be called, there for string will not be evaluated. For example:

```debug("This is a string with evaluation ${iterator.concatenate().allTheItems()}")``` - this will evaluate call and create the string before calling `debug()``.

```debug{ "This is a string with evaluation ${iterator.concatenate().allTheItems()}" }``` - this will postpone evaluation and string creation until a log call will pass a filter, if it will pass.
