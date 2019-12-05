# Loading Indicator

A library allows you to show loading indicator view.

## Usage

```kotlin
val indicator = builder.build {
    setStyle(LoadingIndicator.Style.CUSTOM /* Default is .SYSTEM */)
    setTitle("Loading...")
}.present()
```

> `.SYSTEM` style will adapt colors for current appearance

On Android this builder needs an activity fragment:

```kotlin
val indicator = AndroidLoadingIndicator.Builder(activityFragment)
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

On iOS this builder should be instantiated with `UIViewController`:

```swift
let indicator = IOSLoadingIndicator.Builder(viewController)
```

Define your Color Sets in project's assets if using `.CUSTOM` style:

- `li_colorBackground` for surface color
- `li_colorAccent` for progress bar color

The `LoadingIndicator` interface has implementation on the Android as `AndroidLoadingIndicator`
and on the iOS as `IOSLoadingIndicator`.

The `LoadingIndicator` has methods to show and dismiss a loading indicator:
- `present(animated: Boolean: true, completion: () -> Unit = {}): LoadingIndicator` — show
- `dismiss(animated: Boolean = true, completion: () -> Unit = {})` — dismiss
- `dismissAfter(timeMillis: Long, aniamted: Boolean = true)` — dismiss after `timeMillis` milliseconds
