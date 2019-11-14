# Loading Indicator

A library allows you to show loading indicator view.

## Usage

The `LoadingIndicator` interface has implementation on the Android as `AndroidLoadingIndicator`
and on the iOS as `IOSLoadingIndicator`.

The nested `Builder` class to build indicator.

The `LoadingIndicator` has methods to show and dismiss a loading indicator:
- `present(animated: Boolean: true, completion: () -> Unit = {}): LoadingIndicator`
- `dismiss(animated: Boolean = true, completion: () -> Unit = {})`

### Android

On Android this builder needs an activity fragment:

```kotlin
val indicator = AndroidLoadingIndicator
    .Builder(activityFragment)
    .create()
    .present()
```

### iOS

On iOS this builder should be instantiated with `UIViewController`:

```swift
let indicator = IOSLoadingIndicator
    .Builder(viewController)
    .create()
    .present(animated: true) { }
```
