## Loading Indicator

A library allows you to show loading indicator with custom view.

### Usage
The `LoadingIndicator` interface has implementation on the Android as `AndroidLoadingIndicator`
and on the iOS as `IOSLoadingIndicator`.

The nested `Builder` class to build indicator.

The `LoadingIndicator` has methods to show and dismiss a loading indicator:
- `present(controller: Controller, animated: Boolean: true, completion: () -> Unit = {}): LoadingIndicator`
- `dismiss(animated: Boolean = true, completion: () -> Unit = {})`

On the Android `Controller` is `ActivityFragment` and on the iOS it's `UIViewController`.

#### Android

On Android this builder needs a resource id as custom view for an indicator:

```kotlin
val indicator = AndroidLoadingIndicator
    .Builder(R.layout.activity)
    .create()
    .present(mActivity)
```

This resource will be inflated by custom `DialogFragment` inside library.

#### iOS

On iOS this builder should be instantiated with `UIViewController` to display:

```swift
let indicator = IOSLoadingIndicator
    .Builder(loadingVC)
    .create()
    .present(controller: viewController, animated: true) { }
```

This view controller will be shown over full screen using cross dissolve modal transition style.
