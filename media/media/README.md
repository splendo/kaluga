# Media

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  | ✅ | ✅ | ✅ | ✅ |  |

This Library for Kaluga contains methods for playing audio and video files from local and remote sources.

## Installing
This library is available on Maven Central. You can import Kaluga Review as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.media:media:$kalugaVersion")
}
```

## Usage
Create a `MediaPlayer` object. Then load a `MediaSource` and start playback:

```kotlin
val mediaPlayer = DefaultMediaPlayer(null, baseMediaManagerBuilder, Dispatchers.Main)
val source = mediaSourceFromUrl("")!!
mediaPlayer.initializeFor(source)
mediaPlayer.play()
mediaPlayer.end()
```

Additional controls can be queried using `MediaPlayer.controls`.

**Note** that you must always call `MediaPlayer.end()` when done with media playback so resources can be properly cleaned up.

### Media Source
The platform supports Audio and Video playback from both local and remote sources.
Create a `MediaSource` to start playback.
Local files requires platform specific `MediaSource` creation, though `mediaSourceFromUrl` can be used for any url resource.

### Video Playback
To play video files the player renders onto a platform-specific `MediaSurface`. The view that hosts the
surface binds it through a `MediaSurfaceBinder`: pass the binder to the media manager (`BaseMediaManager`)
and call `MediaSurfaceBinder.bind(surface)` once the surface is available, and `MediaSurfaceBinder.unbind()`
when it goes away. The bound surface is automatically forwarded to `renderVideoOnSurface`.

Create a `MediaSurface` from the platform view that renders the video:
- **Android** — wraps the `SurfaceHolder` of a `SurfaceView`.
- **iOS** — `MediaSurface(viewController: AVPlayerViewController)`.
- **macOS** — `MediaSurface(playerView: AVPlayerView)`.
- **JS / WasmJS** — `MediaSurface(elementId)` for an HTML media element.

When using Compose, the [`media-compose`](../compose) module's `MediaSurfaceContainer(binder)` hosts the
surface and performs the binding for you.

## Testing
Use the [`test-utils-media` module](../test-utils) to get a mockable `MediaPlayer`, `MediaManager`,`BaseMediaManager`, `VolumeController`, `MediaSurfaceController`, `PlayableMedia`, `MediaSurfaceBinder`, `PlaybackState`, and `BasePlaybackStateRepo`.