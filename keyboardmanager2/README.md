# Keyboard Manager

A library allows you to show and dismiss the System keyboard.
It is Activity dependent on Android.

## Usage

Showing and hiding the keyboard is done through the KeyboardManager.

````kotlin
// Shared code
fun showKeyboard(builder: KeyboardManagerBuilder, view: KeyboardView) {
    val keyboardManager = builder.create()
    keyboardManager.show(view)
}

fun hideKeyboard(builder: KeyboardManagerBuilder) {
    val keyboardManager = builder.create()
    keyboardManager.hide()
}
```

The `KeyboardManagerBuilder` and `KeyboardView` are provided on the platform. On Android the builder is created for an `Activity`, where the keyboard view is any `android.view.View` attached to the activity.

````kotlin
// In Activity
val builder = KeyboardManagerBuilder(this)
val keyboardView = this.findById(R.id.some_view)
````

In iOS the builder is attached to a `UIApplication`. By default this will be ` UIApplication.sharedApplication`, but it can be overwritten by a custom Application. The `KeyboardView` can be any `UIView` that `canBecomeFirstResponder`.

```kotlin
val defaultBuilder = KeyboardManagerBuilder()
val customBuilder = KeyboardManagerBuilder(application)
val keyboardView = UITextField()
```


