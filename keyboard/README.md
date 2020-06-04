# Keyboard Manager

A library allows you to show and dismiss the System keyboard.
It is Activity dependent on Android.

## Usage

Showing and hiding the keyboard is done through the KeyboardManager.

````kotlin
// Shared code
fun showKeyboard(builder: KeyboardManagerBuilder, view: KeyboardHostingView) {
    val keyboardManager = builder.create()
    keyboardManager.show(view)
}

fun hideKeyboard(builder: KeyboardManagerBuilder) {
    val keyboardManager = builder.create()
    keyboardManager.hide()
}
```

The `KeyboardManagerBuilder` and `KeyboardHostingView` are provided on the platform. On Android the builder is created for an `Activity`, where the keyboardHostingView is any `android.view.View` attached to the activity.

````kotlin
// In Activity
val builder = KeyboardManagerBuilder(this)
val keyboardHostingView = this.findById(R.id.some_view)
````

In iOS the builder is attached to a `UIApplication`. By default this will be ` UIApplication.sharedApplication`, but it can be overwritten by a custom Application. The `KeyboardHostingView` can be any `UIView` that `canBecomeFirstResponder`.

```kotlin
val defaultBuilder = KeyboardManagerBuilder()
val customBuilder = KeyboardManagerBuilder(application)
val keyboardHostingView = UITextField()
```


