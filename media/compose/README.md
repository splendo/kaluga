# Media Compose

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  | ✅ | ✅ |  |  |

Compose Multiplatform extensions for [`media`](../media): hosts the platform-native video surface inside the Compose tree.

## Installing
This library is available on Maven Central. You can import Kaluga Media Compose as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.media:compose:$kalugaVersion")
}
```

## Usage

A `MediaPlayer` exposes a `MediaSurfaceBinder` as `surfaceBinder`. Render it inside Compose via
`MediaSurfaceContainer`, which creates the platform surface, binds it to the binder, and unbinds it
again on composition exit.

```kotlin
val mediaPlayer = DefaultMediaPlayer(baseMediaManagerBuilder, Dispatchers.Main)

@Composable
fun VideoScreen() {
    MediaSurfaceContainer(binder = mediaPlayer.surfaceBinder, modifier = Modifier.fillMaxSize())
}
```

This is the Compose-hosted counterpart to binding a `MediaSurface` from a platform view (an `Activity`'s
`SurfaceView`, an `AVPlayerViewController`, …) — useful when the host is Compose.

### Platform notes

- **Android** — wraps an `AndroidView { SurfaceView(...) }` and binds its `SurfaceHolder` through the `MediaSurfaceBinder`.
- **iOS** — hosts an `AVPlayerView`-backed `UIView` via `UIKitView`.
- **macOS** — placeholder. CMP-macOS-Native (1.11) ships no `NSView` interop, so video playback inside Compose-on-macOS is not yet supported. Instead create a native `MediaSurface(playerView)` from an AppKit `AVPlayerView` (hosted outside the Compose tree) and bind it via a `MediaSurfaceBinder` from [`media`](../media).
