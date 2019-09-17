## Logging

This is logging simplification support for in `kotlin-mpp` wrapping the [Hydra-log](https://github.com/PocketByte/kotlin-hydra-log).

### How to

#### WARNING!!!
* Because of Hydra-log limitation logger can be initialized only once. All subsequent calls to initialize will return first logger used for initialization.
* If `initLogger(logger: Logger)` was not called before any logging calls, then default logger will be instantiated.

#### Initialization

*Optionally* call `initLogger(logger: Logger): Logger?` with instance of `Logger`. 

If you need to get logger that was used for initialisation, then you can call `logger(): Logger?`

#### Usage

Here's a list of funnctions available for usage:

* `debug(message)` \\ `debug(tag, message)` \\ `debug(exception)` \\ `debug(tag, exception)` \\ `debug(tag, () -> String)`
* `info(message)` \\ `info(tag, message)` \\ `info(exception)` \\ `info(tag, exception)` \\ `info(tag, () -> String)`
* `error(message)` \\ `error(tag, message)` \\ `error(exception)` \\ `error(tag, exception)` \\ `error(tag, () -> String)`
* `warn(message)` \\ `warn(tag, message)` \\ `warn(exception)` \\ `warn(tag, exception)` \\ `warn(tag, () -> String)`
* `log(level, message)` \\ `log(level, tag, message)` \\`log(level, tag, () -> String)`
