# Keyboard Manager

A library allows you to show and dismiss the System keyboard.
It is Activity dependent on Android.

## Usage

Showing and hiding the keyboard is done through the KeyboardManager.

````kotlin
// Shared code
fun showKeyboard(builder: KeyboardManager.Builder, view: KeyboardHostingView) {
    val keyboardManager = builder.create(coroutineScope)
    keyboardManager.show(view)
}

fun hideKeyboard(builder: KeyboardManager.Builder) {
    val keyboardManager = builder.create(coroutineScope)
    keyboardManager.hide()
}
```

The `KeyboardManager.Builder` and `KeyboardHostingView` are provided on the platform.

On Android the builder uses a `LifecycleManagerObserver` (see Architecture), where the keyboardHostingView is any resource Id for a `View` attached to the `Activity` bound to the manager.

````kotlin
// In Activity
val builder = KeyboardManager.Builder()
val keyboardHostingView = R.id.some_view
````

In iOS the builder is attached to a `UIApplication`. By default this will be ` UIApplication.sharedApplication`, but it can be overwritten by a custom Application. The `KeyboardHostingView` can be any `UIView` that `canBecomeFirstResponder`.

```kotlin
val defaultBuilder = KeyboardManagerBuilder()
val customBuilder = KeyboardManagerBuilder(application)
val keyboardHostingView = UITextField()
```


