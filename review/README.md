## Review
This Library for Kaluga contains methods for requesting the user to review the app.
Both Android and iOS will limit the amount of times this dialog is actually shown.
As such, this library will only guarantee that an attempt will be made to show the request review dialog.

## Installing
To import this library add the Kaluga Bintray as a maven dependency `https://dl.bintray.com/kaluga/com.splendo.kaluga/`. You can then import Kaluga Resources as follows:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
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

On Android the builder is a `LifecycleSubscribable` (see Architecture) that needs a `LifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the review manager.
For `BaseViewModel`, the builder should be made **publicly** visible and bound to a `KalugaViewModelLifecycleObserver`.

```kotlin
class ReviewViewModel: BaseViewModel() {

    val builder = ReviewManager.Builder()

    fun attemptToRequestReview() {
        coroutineScope.launch {
            viewModel.builder.create().attemptToRequestReview()
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

For other usages, make sure to call `LifecycleSubscriber.subscribe` and `LifecycleSubscriber.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = ReviewManager.Builder()
    builder.subscribe(activity)
    builder.create(coroutineScope).attemptToRequestReview()
}
```

You can use the `AppCompatActivity.reviewManager()` convenience method to get a builder that is valid during the lifespan of the Activity it belongs to.

### iOS
On iOS simply create the builder

```swift
let builder = ReviewManager.Builder()
```
