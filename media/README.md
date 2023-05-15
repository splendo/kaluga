# Media
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
    implementation("com.splendo.kaluga:media:$kalugaVersion")
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
To play video files, the MediaPlayer should be bound to a platform specific `MediaSurface`.
Call `MediaPlayer.renderVideoOnSurface()` to bind to a surface.
The `BaseMediaManager` class offers a way to inject a `MediaSurfaceProvider` that can be bound to the lifecycle to automatically provide a `MediaSurface`.

On **Android** create an `ActivityMediaSurfaceProvider` that provides a `MediaSurface` for a given `Activity`.
On **iOS** multiple `BaseMediaSurfaceProvider` are available: `UIViewMediaSurfaceProvider`, `AVPlayerLayerMediaSurfaceProvider`, `AVPlayerViewControllerMediaSurfaceProvider`, and `BindingMediaSurfaceProvider`

## Testing
Use the [`test-utils-media` module](../test-utils-media) to get a mockable `MediaPlayer`, `MediaManager`,`BaseMediaManager`, `VolumeController`, `MediaSurfaceController`, `PlayableMedia`, `MediaSurfaceProvider`, `PlaybackState`, and `BasePlaybackStateRepo`.