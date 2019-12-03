# Alerts

A library allows you to show native alerts.
It shows `AlertDialog` on Android and `UIAlertController` on iOS.

## Usage

Using Alerts is very simple. You can show an keyboardManager from shared code like this:

```kotlin
// Shared code
fun showAlert(builder: AlertBuilder, title: String) = MainScope().launch(MainQueueDispatcher) {
    // Create OK action
    val okAction = Alert.Action("OK") // POSITIVE/DEFAULT style
    // Create Cancel action
    val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
    // Create an Alert with title, message and actions
    val keyboardManager = builder.buildAlert {
        setTitle(title)
        setMessage("This is sample message")
        addActions(okAction, cancelAction)
    }
    // Show and handle action
    when (keyboardManager.show()) {
        okAction -> println("OK pressed")
        cancelAction -> println("Cancel pressed")
    }
}
```

Or this:

```kotlin
// Shared code
fun showAlert(builder: AlertBuilder, title: String) = MainScope().launch(MainQueueDispatcher) {
    // Create an Alert with title, message and actions
    val keyboardManager = builder.buildAlert {
        setTitle(title)
        setPositiveButton("Yes") { /* handle `Yes` action */ }
        setNegativeButton("No") { /* handle `No` action */ }
    }
    // Show
    keyboardManager.show()
}
```

> You should use `launch` with built-in `MainQueueDispatcher` dispatcher.

But you have to prepare `AlertBuilder` object from specific platform.
On Android this builder needs a `Context` object:

```kotlin
// Android specific
val builder = AlertBuilder(context)
SharedKt.showAlert(builder, "Hello from Android")
```

On iOS this builder should be instantiated with `UIViewController`:

```swift
// iOS specific
let builder = AlertsAlertBuilder(viewController)
SharedKt.showAlert(builder, "Hello from iOS")
```

You can also show action sheet using Actions with handlers:

```kotlin
fun showList(builder: AlertBuilder) = MainScope().launch(MainQueueDispatcher) {
    builder.buildActionSheet {
        setTitle("Select an option")
        addActions(
            Alert.Action("Option 1") { /* handle option #1 */ },
            Alert.Action("Option 2") { /* handle option #2 */ },
            Alert.Action("Option 3") { /* handle option #3 */ },
            Alert.Action("Option 4") { /* handle option #4 */ }
        )
    }.show()
}
```
> Cancel action will be added automatically on iOS platform
> You should use `launch` with built-in `MainQueueDispatcher` dispatcher.

In order to dismiss keyboardManager you can use `dismiss()` function:

```kotlin
// Build keyboardManager
val keyboardManager = builder.buildAlert {
    setTitle("Please wait...")
    setPositiveButton("OK")
}
// Show
keyboardManager.show()
// Dismiss
keyboardManager.dismiss()
```

## Builder

The `BaseAlertBuilder` abstract class has implementations on the Android as `AlertBuilder` and iOS as `AlertsAlertBuilder`.

### Build keyboardManager

- `buildAlert(initialize: BaseAlertBuilder.() -> Unit): AlertInterface` — builder to create `AlertInterface`, thread-safe

### Build action sheet

- `buildActionSheet(initialize: BaseAlertBuilder.() -> Unit): AlertInterface` — builder to create `AlertInterface`, thread-safe

### Set title, style and message

- `setTitle(title: String?)` — sets optional title for the keyboardManager
- `setMessage(message: String?)` — sets an optional message for the keyboardManager

### Set buttons

- `setPositiveButton(title: String, handler: AlertActionHandler)` — sets a positive button for the keyboardManager
- `setNegativeButton(title: String, handler: AlertActionHandler)` — sets a negative button for the keyboardManager
- `setNeutralButton(title: String, handler: AlertActionHandler)` — sets a neutral button for the keyboardManager

> On Android you can have only maximum of 3 buttons (each of type Positive, Negative and Neutral) for the keyboardManager with Alert.Style.ALERT

### Set actions for action sheet

- `addActions(actions: List<Alert.Action>)` or `addActions(vararg actions: Alert.Action)` — adds a list of actions for the keyboardManager

> On iOS you can have only 1 button with type of Action.Style.Cancel / Negative

#### Action styles

On Android actions can be: `Positive`, `Negative` and `Neutral`.
On iOS actions can be: `Default`, `Cancel` and `Destructive`.
