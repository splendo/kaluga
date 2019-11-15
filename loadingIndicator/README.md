# Loading Indicator

A library allows you to show loading indicator view.

## Usage

```kotlin
val indicator = builder.build {
    setStyle(LoadingIndicator.Style.LIGHT /* .DARK, default is .SYSTEM */)
}.present()
```

> `.SYSTEM` style will adapt colors for current appearance

On Android this builder needs an activity fragment:

```kotlin
val indicator = AndroidLoadingIndicator.Builder(activityFragment)
```

On iOS this builder should be instantiated with `UIViewController`:

```swift
let indicator = IOSLoadingIndicator.Builder(viewController)
```

The `LoadingIndicator` interface has implementation on the Android as `AndroidLoadingIndicator`
and on the iOS as `IOSLoadingIndicator`.

The `LoadingIndicator` has methods to show and dismiss a loading indicator:
- `present(animated: Boolean: true, completion: () -> Unit = {}): LoadingIndicator`
- `dismiss(animated: Boolean = true, completion: () -> Unit = {})`
