# HUD

A library allows you to show HUD (e.g. loading indicator) view.

## Installing
This library is available on Maven Central. You can import Kaluga HUD as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:hud:$kalugaVersion")
}
```

## Usage

Show default HUD:

```kotlin
MainScope().launch {
    val hud = builder.build(this)
    hud.present()
    // do Stuff
    hud.dismiss()
}
```

> Default style is `.SYSTEM` and it will adapt colors for current appearance (dark/light)
> Default HUD also has no title label

Custom HUD with title:

```kotlin
MainScope().launch {
    val hud = builder.build(this) {
        setStyle(HUDStyle.CUSTOM)
        setTitle("Loading...")
    }
    hud.present()
}
```

The `HUD` has methods to show and dismiss a loading indicator view:
- `present(animated: Boolean = true): HUD` — show
- `dismiss(animated: Boolean = true)` — dismiss
- `dismissAfter(timeMillis: Long, animated: Boolean = true): HUD` — dismiss after `timeMillis` milliseconds
- `presentDuring(block: suspend HUD.() -> T): T` — show and keep visible during `block` execution, returning the result `T` of the block after dismissal.

## Builder

The `HUD.Builder` class can be used to build HUDs.

- `build(coroutineScope: CoroutineScope, initialize: HudConfig.Builder.() -> Unit): BaseHUD` — builder to create `BaseHUD`, thread-safe
- 
## Platform Specific Building
The `HUD.Builder` object should be created from the platform side.

### Android
On Android the builder is an `ActivityLifecycleSubscribable` (see Architecture) that needs an `ActivityLifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the HUD.
For `BaseLifecycleViewModel`, the builder should be provided to `BaseLifecycleViewModel.activeLifecycleSubscribables` (using the constructor or `BaseLifecycleViewModel.addLifecycleSubscribables`) and bound to a `KalugaViewModelLifecycleObserver` or `ViewModelComposable`.

```kotlin
class HudViewModel: BaseLifecycleViewModel() {

    val builder = HUD.Builder()

    init {
        addLifecycleSubscribables(builder)
    }

    fun present() {
        coroutineScope.launch {
            viewModel.builder.build(this) {
              setStyle(HUDStyle.CUSTOM)
              setTitle("Loading...")
            }.present()
        }
    }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: KalugaViewModelActivity<HudViewModel>() {

    override val viewModel: HudViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.present()
    }

}
```

For other usages, make sure to call `ActivityLifecycleSubscribable.subscribe` and `ActivityLifecycleSubscribable.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = HUD.Builder()
    builder.subscribe(activity)
    builder.build(coroutineScope) {
        // HUD Logic
    }.present()
}
```

Define your custom colors inside `colors.xml` if using `.CUSTOM` style:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- surface color -->
    <color name="li_colorBackground">#FFFFFF</color>
    <!-- progress bar color -->
    <color name="li_colorAccent">#000000</color>
</resources>
```

### iOS

On iOS this builder should be instantiated with `UIViewController`:

```swift
let builder = HUD.Builder(viewController)
```

Since a `UIViewController` is required, for SwiftUI the `View` displaying the HUD should have a `UIViewControllerRepresentable` wrapping the `UIViewController` associated with the `HUD.Builder` attached.
The [Kaluga SwiftUI scripts](https://github.com/splendo/kaluga-swiftui) provide a `ContainerView` that offers this functionality out of the box (if the `includeHud` setting is set to `true`)

Define your Color Sets in project's assets if using `.CUSTOM` style:

- `li_colorBackground` for surface color
- `li_colorAccent` for progress bar / activity indicator color

## Testing
Use the [`test-utils-hud` module](../test-utils-hud) to get a mockable `HUDBuilder`.
