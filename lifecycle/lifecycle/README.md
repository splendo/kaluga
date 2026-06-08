# Lifecycle
Defines the cross-platform `LifecycleSubscribable` marker and its per-platform host bindings (`ActivityLifecycleSubscribable` on Android, `ViewControllerLifecycleSubscribable` on iOS, `WindowLifecycleSubscribable` on macOS).
Service builders (alerts, HUD, keyboard, …) implement one of these so they can be wired to the current host without leaking platform types into common code.

## Installing
This library is available on Maven Central. You can import Kaluga Lifecycle as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.lifecycle:lifecycle:$kalugaVersion")
}
```

## Usage

A common-source consumer holds a `LifecycleSubscribable`. The platform side provides the host handle and calls `subscribe` / `unsubscribe`.

### Android
The Android binding is an `ActivityLifecycleSubscribable`. Its `LifecycleManager` carries the `Activity`, `LifecycleOwner`, and `FragmentManager`. Subscribe convenience methods exist for `AppCompatActivity` and `Fragment`:

```kotlin
class MyActivity : AppCompatActivity() {
    private val builder = SomeBuilder() // implements ActivityLifecycleSubscribable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        builder.subscribe(this)
    }

    override fun onDestroy() {
        builder.unsubscribe()
        super.onDestroy()
    }
}
```

`LifecycleManagerObserver` is a default implementation that exposes the current manager as a `StateFlow` (`managerState`) — use it when you want to react to subscribe/unsubscribe events rather than poll.

### iOS
The iOS binding is a `ViewControllerLifecycleSubscribable` carrying a `UIViewController`:

```kotlin
val builder = SomeBuilder() // implements ViewControllerLifecycleSubscribable
builder.subscribe(viewController)
// later
builder.unsubscribe()
```

### macOS
The macOS binding is a `WindowLifecycleSubscribable` carrying an AppKit `NSWindow`:

```kotlin
val builder = SomeBuilder() // implements WindowLifecycleSubscribable
builder.subscribe(window)
// later
builder.unsubscribe()
```

## Compose
For Compose Multiplatform hosts the wiring is automated by the [`lifecycle-compose` module](../lifecycle-compose), which exposes `LifecycleSubscribable.AttachToCompose()` and dispatches to the correct subtype for the current platform.

## Architecture integration
The [`architecture` module](../architecture)'s `BaseLifecycleViewModel` exposes `activeLifecycleSubscribables`. Any `LifecycleSubscribable` added there is automatically wired by `KalugaViewModelLifecycleObserver` (legacy MVVM path) or `ViewModelComposable` (Compose path).
