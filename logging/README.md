## Logging

This module provided simplified access to logging capabilities of [Kotlin Kydra Log](https://github.com/PocketByte/kotlin-kydra-log).

## Installing
To import this library add the Kaluga Bintray as a maven dependency `https://dl.bintray.com/kaluga/com.splendo.kaluga/`. You can then import Kaluga Logging as follows:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:logging:$kalugaVersion")
}
```

### How to

#### NOTE
* Because of Kydra Log limitation logger can be initialized only once. All subsequent calls to initialize will return first logger used for initialization.
* If `initLogger(logger: Logger)` was not called before any logging calls, then default logger will be instantiated.

#### Initialization

*Optionally* call `initLogger(logger: Logger): Logger` with instance of `Logger`. 

If you need to get logger that was used for initialisation, then you can call `logger(): Logger`

#### Usage

Here's a list of functions available for usage:

* `debug(message)` \\ `debug(tag, message)` \\ `debug(exception)` \\ `debug(tag, exception)` \\ `debug(tag, () -> String)`
* `info(message)` \\ `info(tag, message)` \\ `info(exception)` \\ `info(tag, exception)` \\ `info(tag, () -> String)`
* `error(message)` \\ `error(tag, message)` \\ `error(exception)` \\ `error(tag, exception)` \\ `error(tag, () -> String)`
* `warn(message)` \\ `warn(tag, message)` \\ `warn(exception)` \\ `warn(tag, exception)` \\ `warn(tag, () -> String)`
* `log(level, message)` \\ `log(level, tag, message)` \\`log(level, tag, () -> String)`

##### Message or `()->String`?

In case of string template usage consider using `()->String`. If logging call will be filtered out, then function providing message will not be called, there for string will not be evaluated. For example:

```debug("This is a string with evaluation ${iterator.concatenate().allTheItems()}")``` - this will evaluate call and create the string before calling `debug()``.

```debug{ "This is a string with evaluation ${iterator.concatenate().allTheItems()}" }``` - this will postpone evaluation and string creation until a log call will pass a filter, if it will pass.
