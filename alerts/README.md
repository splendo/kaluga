## Alerts

A library allows you to show native alerts.
It shows `AlertDialog` on Android and `UIAlertController` on iOS.

### Usage
The `BaseAlertBuilder` abstract class has implementations on the Android as `AlertBuilder` and iOS as `AlertsAlertBuilder`.
It has methods:
- `suspend alert(initialize: BaseAlertBuilder.() -> Unit): AlertInterface` — builder to create `AlertInterface`, thread-safe
- `buildUnsafe(initialize: BaseAlertBuilder.() -> Unit): AlertInterface` — build `AlertInterface`, not thread-safe
- `setTitle(title: String?)` — sets optional title for the alert
- `setStyle(style: Alert.Style)` - sets the type of alert to display (An alert or an action sheet/list)
- `setMessage(message: String?)` — sets an optional message for the alert
- `setPositiveButton(title: String, handler: AlertActionHandler)` — sets a positive button for the alert
- `setNegativeButton(title: String, handler: AlertActionHandler)` — sets a negative button for the alert
- `setNeutralButton(title: String, handler: AlertActionHandler)` — sets a neutral button for the alert
- `addActions(actions: List<Alert.Action>)` or `addActions(vararg actions: Alert.Action)` — adds a list of actions for the alert

#### Alert styles

There are two different Alert styles:
- `Alert.Style.ALERT` — a standard alert type
- `Alert.Style.ACTION_SHEET` — an action sheet type

Default alert requires a title or message to be set,
but for action sheet it is not necessary.

#### Action styles

On Android actions can be: `Positive`, `Negative` and `Neutral`.
On iOS actions can be: `Default`, `Cancel` and `Destructive`.

#### Builder

On Android this builder needs a `Context` object:

```kotlin
val builder = AlertBuilder(context)
```

On iOS this builder should be instantiated with `UIViewController`:

```swift
let builder = AlertsAlertBuilder(viewController)
```

The `AlertInterface` has methods to show and dismiss alert:
- `showAsync(animated: Boolean = true, completion: () -> Unit = {})`
- `suspend show(animated: Boolean = true): Alert.Action?`
- `dismiss(animated: Boolean = true)`

### Examples

#### Using Kotlin coroutines

Create an Alert:
```kotlin
val alert = AlertBuilder(context).alert {
    setTitle("Hello, Kaluga")
    setPositiveButton("OK") { println("OK pressed") }
    setNegativeButton("Cancel") { println("Cancel pressed") }
    setNeutralButton("Details") { println("Details pressed") }
}
```

In order to show alert use `alert.show()` call. Alert will be dismissed after the user pressed a button.
Dialog is cancellable, so the user also can tap outside of the alert or press back button to dismiss.
You also can dismiss it in code with `alert.dismiss()` call.

Before use on iOS platform you have to wrap `suspend` call
inside `MainScope().launch` with `MainQueueDispatcher`:

```kotlin
fun showAlert(builder: AlertBuilder, title: String) = MainScope().launch(MainQueueDispatcher) {
    // Create OK action
    val okAction = Alert.Action("OK") // POSITIVE/DEFAULT style
    // Create Cancel action
    val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
    // Create an Alert with title, message and actions
    val alert = builder.alert {
        setTitle(title)
        setMessage("This is sample message")
        addActions(okAction, cancelAction)
    }
    // Show and handle actions
    when (alert.show()) {
        okAction -> println("OK pressed")
        cancelAction -> println("Cancel pressed")
    }
}
```

Then call with platform-specific builder:

```swift
let builder = AlertBuilder(viewController: viewController)
SharedKt.showAlert(builder, "Hello from iOS")
```

#### Without Kotlin coroutines

If you use shared builder object this call is not thread-safe:

```kotlin
val alert = builder.buildUnsafe {
    setTitle("Hello")
    setStyle(Alert.Style.ACTION_SHEET)
    setActions(action1, action2)
}.showAsync()
```
