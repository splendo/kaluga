# Test Utils Media

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ |  |

This library adds support for testing the [`media` module](../media) to [`test-utils`](../../base/test-utils)

## Installing
This library is available on Maven Central. You can import Kaluga Test Utils Media as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.media:test:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `MediaPlayer`, `MediaManager`,`BaseMediaManager`, `VolumeController`, `MediaSurfaceController`, `PlayableMedia`, `MediaSurfaceProvider`, `PlaybackState`, and `BasePlaybackStateRepo`.
