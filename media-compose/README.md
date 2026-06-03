# Media Compose
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
    implementation("com.splendo.kaluga:media-compose:$kalugaVersion")
}
```

## Usage

Create a `ComposeMediaSurfaceProvider`, hand it to a `MediaManager`, and render it inside Compose via `MediaSurfaceContainer`. The container creates the platform surface and pushes it into the provider; the surface is detached on composition exit.

```kotlin
val provider = ComposeMediaSurfaceProvider()
val mediaPlayer = DefaultMediaPlayer(provider, baseMediaManagerBuilder, Dispatchers.Main)

@Composable
fun VideoScreen() {
    MediaSurfaceContainer(provider, modifier = Modifier.fillMaxSize())
}
```

This is the inverse of the platform "walk the view tree to find a `SurfaceView`" providers — useful when the host is Compose rather than an `Activity` or `UIViewController`.

### Platform notes

- **Android** — wraps an `AndroidView { SurfaceView(...) }` and pushes its `SurfaceHolder` into the provider.
- **iOS** — hosts an `AVPlayerView`-backed `UIView` via `UIKitView`.
- **macOS** — placeholder. CMP-macOS-Native (1.11) ships no `NSView` interop, so video playback inside Compose-on-macOS is not yet supported. Use the native `NSViewMediaSurfaceProvider` / `WindowLifecycleSubscribable` from [`media`](../media) against an AppKit `AVPlayerView` hosted outside the Compose tree.
