# HUD

A library allows you to show HUD (e.g. loading indicator) view.

## Usage

Show default HUD:
```kotlin
val hud = builder.build().present()
```

> Default style is `.SYSTEM` and it will adapt colors for current appearance
> Default HUD has no title label

Custom with title:

```kotlin
val hud = builder.build {
    setStyle(HUD.Style.CUSTOM)
    setTitle("Loading...")
}.present()
```

The `HUD` interface has implementation on the Android as `AndroidHUD`
and on the iOS as `IOSHUD`.

The `HUD` has methods to show and dismiss a loading indicator:
- `present(animated: Boolean: true, completion: () -> Unit = {}): HUD` — show
- `dismiss(animated: Boolean = true, completion: () -> Unit = {})` — dismiss
- `dismissAfter(timeMillis: Long, animated: Boolean = true)` — dismiss after `timeMillis` milliseconds
- `setTitle(title: String?)` — sets title on already presented hud

### Android

On Android this builder needs an activity fragment:

```kotlin
val hud = AndroidHUD.Builder(activityFragment)
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
let hud = IOSHUD.Builder(viewController)
```

Define your Color Sets in project's assets if using `.CUSTOM` style:

- `li_colorBackground` for surface color
- `li_colorAccent` for progress bar color
