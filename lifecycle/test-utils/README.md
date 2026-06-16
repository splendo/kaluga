# Test Utils Lifecycle

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

This library adds support for testing the [`lifecycle` module](../lifecycle) to [`test-utils`](../../base/test-utils).

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
- `AppCompatActivity.lifecycleManagerObserver()` — convenience that returns a per-activity `LifecycleManagerObserver` subscribed to that activity's lifecycle. Used by the `*Builder()` helpers in `alerts/test-utils`, `hud/test-utils`, `keyboard/test-utils`, and `date-time-picker/test-utils`.
