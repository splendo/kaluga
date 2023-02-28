# Review
This Library for Kaluga contains methods for requesting the user to review the app.
Both Android and iOS will limit the amount of times this dialog is actually shown.
As such, this library will only guarantee that an attempt will be made to show the request review dialog.

## Installing
This library is available on Maven Central. You can import Kaluga Review as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:review:$kalugaVersion")
}
```

## Usage
Create a `ReviewManager` through a `ReviewManager.Builder` and then request the dialog to be showing using `attemptToRequestReview`.

```kotlin
fun requestReview() {
    coroutineScope.launch {
        builder.create().attemptToRequestReview()
    }
}
```

### Android
The Android ReviewManager can only be tested on apps that have been installed though the Playstore.
Use an internal testing track to validate this library.

In addition, Android allows you to pass a ReviewManager.Type.
For production apps this should refer to `Live`, though a `Fake` type may be passed for testing purposes.
Note that Fake will still not show a Review dialog on apps not installed though the Playstore.

On Android the builder is an `ActivityLifecycleSubscribable` (see Architecture) that needs an `ActivityLifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the review manager.
For `BaseLifecycleViewModel`, the builder should be provided to `BaseLifecycleViewModel.activeLifecycleSubscribables` (using the constructor or `BaseLifecycleViewModel.addLifecycleSubscribables`) and bound to a `KalugaViewModelLifecycleObserver` or `ViewModelComposable`.

```kotlin
class ReviewViewModel(val builder: ReviewManager.Builder): BaseLifecycleViewModel(builder) {
    
    fun attemptToRequestReview() {
        coroutineScope.launch {
            builder.create().attemptToRequestReview()
        }
    }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: KalugaViewModelActivity<ReviewViewModel>() {

    override val viewModel: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.attemptToRequestReview()
    }

}
```

For other usages, make sure to call `ActivityLifecycleSubscribable.subscribe` and `ActivityLifecycleSubscribable.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = ReviewManager.Builder()
    builder.subscribe(activity)
    builder.create(coroutineScope).attemptToRequestReview()
}
```

### iOS
On iOS simply create the builder

```swift
let builder = ReviewManager.Builder()
```
