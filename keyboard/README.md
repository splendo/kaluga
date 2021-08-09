# Keyboard Manager

A library allows you to show and dismiss the System keyboard.
It is Activity dependent on Android.

## Installing
To import this library add the Kaluga Bintray as a maven dependency `https://dl.bintray.com/kaluga/com.splendo.kaluga/`. You can then import Kaluga Keyboard as follows:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:keyboard:$kalugaVersion")
}
```

## Usage

Showing and hiding the keyboard is done through the KeyboardManager.

```kotlin
// Shared code
fun showKeyboard(focusHandler: FocusHandler) {
    keyboardManager.show(focusHandler)
}

fun hideKeyboard(builder: KeyboardManager.Builder) {
    keyboardManager.hide()
}
```

The `KeyboardManager.Builder` and `FocusHandler` are provided on the platform.

### Android
On Android the builder is a `LifecycleSubscribable` (see Architecture) that needs a `LifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the keyboard.
For `BaseViewModel`, the builder should be made **publicly** visible and bound to a `KalugaViewModelLifecycleObserver`.
The keyboardHostingView is any resource Id for a `View` attached to the `Activity` bound to the manager.

```kotlin
class KeyboardViewModel(val builder: KeyboardManager.Builder): BaseViewModel() {

    private val keyboardManager = builder.create(coroutineScope)

    fun show(view: FocusHandler) {
        keyboardManager.show(view)
    }

    fun hide() {
        keyboardManager.hide()
    }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: KalugaViewModelActivity<KeyboardViewModel>() {

    override val viewModel = KeyboardViewModel(keyboardManagerBuilder())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.show(AndroidFocusHandler(R.id.some_view))
        viewModel.hide()
    }

}
```

For other usages, make sure to call `LifecycleSubscriber.subscribe` and `LifecycleSubscriber.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = KeyboardManager.Builder()
    builder.subscribe(activity)
    builder.create(coroutineScope).show(AndroidFocusHandler(R.id.some_view))
}
```

You can use the `AppCompatActivity.keyboardManagerBuilder` convenience method to get a builder that is valid during the lifespan of the Activity it belongs to.

### Android compose-ui

Use compose's implementation for `FocusHandler`.
```kotlin

@Composable
fun SomeLayout() {
    val focusHandler = ComposeFocusHandler(FocusRequester.Default)
    TextField (
        value = numberInput,
        onValueChange = numberInputDelegate,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(FocusRequester.Default), // add this line to the TextField Modifier.
    )
    
    Button( { viewModel.show(focusHandler) } ) {
        Text("Button")
    }
}
```

### iOS
In iOS the builder is attached to a `UIApplication`. By default this will be ` UIApplication.sharedApplication`, but it can be overwritten by a custom Application. The `KeyboardHostingView` can be any `UIView` that `canBecomeFirstResponder`.

```kotlin
val defaultBuilder = KeyboardManagerBuilder()
val customBuilder = KeyboardManagerBuilder(application)
val keyboardHostingView = UITextField()
```


