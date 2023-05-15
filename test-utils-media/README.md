# Test Utils Media

This library adds support for testing the [`media` module](../media) to [`test-utils`](../test-utils-base)

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
    implementation("com.splendo.kaluga:test-utils-media:$kalugaVersion")
}
```

## Mocks
This library contains mock classes for `MediaPlayer`, `MediaManager`,`BaseMediaManager`, `VolumeController`, `MediaSurfaceController`, `PlayableMedia`, `MediaSurfaceProvider`, `PlaybackState`, and `BasePlaybackStateRepo`.
