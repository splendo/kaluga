# Test Utils Lifecycle

This library adds support for testing the [`lifecycle` module](../lifecycle) to [`test-utils`](../test-utils-base).

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Lifecycle as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.lifecycle:test:$kalugaVersion")
}
```

## Helpers
On Android this library exposes:

- `AppCompatActivity.getOrPutAndRemoveOnDestroyFromCache` — caches an instance of `T` per activity and removes it on `ON_DESTROY`.
- `AppCompatActivity.lifecycleManagerObserver()` — convenience that returns a per-activity `LifecycleManagerObserver` subscribed to that activity's lifecycle. Used by the `*Builder()` helpers in `test-utils-alerts`, `test-utils-hud`, `test-utils-keyboard`, and `test-utils-date-time-picker`.
