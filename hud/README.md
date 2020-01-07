# HUD

A library allows you to show HUD (e.g. loading indicator) view.

## Usage

Show default HUD:

```kotlin
val hud = builder.build().present()
```

> Default style is `.SYSTEM` and it will adapt colors for current appearance (dark/light)
> Default HUD also has no title label

Custom HUD with title:

```kotlin
val hud = builder.build {
    setStyle(HUD.Style.CUSTOM)
    setTitle("Loading...")
}.present()
```

The `HUD` interface has implementation on the Android as `AndroidHUD` class
and on the iOS as `IOSHUD` class.

The `HUD` has methods to show and dismiss a loading indicator view:
- `present(animated: Boolean = true, completion: () -> Unit = {}): HUD` — show
- `dismiss(animated: Boolean = true, completion: () -> Unit = {})` — dismiss
- `dismissAfter(timeMillis: Long, animated: Boolean = true): HUD` — dismiss after `timeMillis` milliseconds
- `presentDuring(block: suspend () -> Unit)` — show and keep visible during `block` execution

### Android

On Android you need to subscribe the builder to `LifecycleOwner` and pass `FragmentManager`, the best way is to use `HudViewModel`:

```kotlin
open class HudViewModel: ViewModel() {

    val builder = AndroidHUD.Builder()

    fun subscribe(activity: AppCompatActivity) {
        builder.subscribe(activity, activity.supportFragmentManager)
    }

    fun unsubscribe() {
        builder.unsubscribe()
    }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: AppCompatActivity() {

    private val viewModel: HudViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.subscribe(this)
        //
        // Pass viewModel.builder to shared code
        //
    }

    override fun onDestroy() {
        viewModel.unsubscribe()
        super.onDestroy()
    }
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
let builder = IOSHUD.Builder(viewController)
```

Define your Color Sets in project's assets if using `.CUSTOM` style:

- `li_colorBackground` for surface color
- `li_colorAccent` for progress bar / activity indicator color
