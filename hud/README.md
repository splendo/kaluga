# HUD

A library allows you to show HUD (e.g. loading indicator) view.

## Usage

Show default HUD:

```kotlin
val hud = builder.build()
launch {
    hud.present()
    // do Stuff
    hud.dismiss()
}
```

> Default style is `.SYSTEM` and it will adapt colors for current appearance (dark/light)
> Default HUD also has no title label

Custom HUD with title:

```kotlin
val hud = builder.build {
    setStyle(HUDStyle.CUSTOM)
    setTitle("Loading...")
}
launch {
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

### Android
On Android the builder needs a `UIContextObserver` (see Architecture) object to provide the current context in which to display the HUD.
For `BaseViewModel`, the `UIContextObserver` will be automatically provided with the correct context, provided the builder is publicly visible and bound to a `KalugaViewModelLifecycleObserver`.

```kotlin
class HudViewModel: BaseViewModel() {

    val builder = HUD.Builder()

    fun present() {
        coroutineScope.launch {
            viewModel.builder.build {
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

For other usages, make sure to call `UIContextObserver.subscribe` and `UIContextObserver.unsubscribe` to manage the lifecycle manually.

```kotlin
val contextObserver = UIContextObserver()
val builder = AlertBuilder(contextObserver)
contextObserver.subscribe(activity)
launch {
    builder.build {
        setStyle(HUDStyle.CUSTOM)
        setTitle("Loading...")
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

Define your Color Sets in project's assets if using `.CUSTOM` style:

- `li_colorBackground` for surface color
- `li_colorAccent` for progress bar / activity indicator color
