# Lifecycle Compose

| Android | iOS | JVM | JS | WasmJS | macOS | tvOS | watchOS |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| ✅ | ✅ |  |  | ✅ | ✅ |  |  |

Compose Multiplatform wiring for [`lifecycle`](../lifecycle): drives `LifecycleSubscribable.subscribe` / `unsubscribe` from the current Compose host on Android, iOS, and macOS.

## Installing
This library is available on Maven Central. You can import Kaluga Lifecycle Compose as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.lifecycle:compose:$kalugaVersion")
}
```

## Usage

Call `AttachToCompose()` on any `LifecycleSubscribable` inside a composition. Each platform actual dispatches based on the concrete subtype:

- **Android** — `ActivityLifecycleSubscribable` subscribes with the `Activity` from `LocalContext` and the `LifecycleOwner` from `LocalLifecycleOwner`.
- **iOS** — `ViewControllerLifecycleSubscribable` subscribes with `LocalUIViewController.current`.
- **macOS** — `WindowLifecycleSubscribable` subscribes with the `NSWindow` exposed via this module's `LocalNSWindow` composition local.

Any subscribable whose subtype isn't recognised on the current platform is a no-op, so a single common-source builder can be safely attached on every target.

```kotlin
@Composable
fun SomeScreen(builder: SomeBuilder) { // implements one of the LifecycleSubscribable subtypes
    builder.AttachToCompose()
    // ...
}
```

## LifecycleViewModel

`LifecycleViewModel` is an optional `androidx.lifecycle.ViewModel` base class that holds a list of `LifecycleSubscribable`s. The screen-side composable calls `AttachToCompose()` on the ViewModel and all of them get wired in one call:

```kotlin
class SomeViewModel(builder: SomeBuilder) : LifecycleViewModel(subscribables = listOf(builder))

@Composable
fun SomeScreen() {
    val vm = viewModel<SomeViewModel>().AttachToCompose()
    // ...
}
```

Extending `LifecycleViewModel` is not required — any ViewModel can hold a subscribable and call `field.AttachToCompose()` manually.

## macOS host setup

Compose Multiplatform exposes `WindowScope.window` as a receiver but ships no built-in `LocalNSWindow`. Install one at the host's `Window { … }` entry point so subscribables further down the tree can read it:

```kotlin
Window(title = "App") {
    ProvideNSWindow {
        AppRootScreen()
    }
}
```

`WindowLifecycleSubscribable.AttachToCompose()` silently no-ops when `LocalNSWindow` is not installed.
