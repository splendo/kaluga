## Alerts

A library allows you to show native alerts.
It shows `AlertDialog` on Android and `UIAlertController` on iOS.

### Usage
The `BaseAlertBuilder` abstract class has implementations on the Android as `AlertBuilder` and iOS as `AlertsAlertBuilder`.
It has methods:
- `suspend alert(initialize: BaseAlertBuilder.() -> Unit): AlertInterface` — builder to create `AlertInterface`
- `setTitle(title: String?)` — sets optional title for the alert
- `setMessage(message: String?)` — sets an optional message for the alert
- `setPositiveButton(title: String, handler: AlertActionHandler)` — sets a positive button for the alert
- `setNegativeButton(title: String, handler: AlertActionHandler)` — sets a negative button for the alert
- `setNeutralButton(title: String, handler: AlertActionHandler)` — sets a neutral button for the alert
- `addActions(actions: List<Alert.Action>)` — adds a list of actions for the alert

On Android this builder needs a `Context` object:

```kotlin
val builder = AlertBuilder(context)
```

On iOS this builder should be instantiated with `UIViewController`:

```swift
let builder = AlertsAlertBuilder(viewController)
```

The `AlertInterface` has methods to show and dismiss alert:
- `show(animated: Boolean = true, completion: () -> Unit = {})`
- `suspend show(animated: Boolean = true): Alert.Action?`
- `dismiss(animated: Boolean = true)`

### Example

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
Dialog is cancelable, so the user also can tap outside of the alert or press back button to dismiss.
You also can dismiss it in code with `alert.dismiss()` call.

Before use on iOS platform you have to wrap `suspend` calls inside shared Kotlin code like this:

```kotlin
fun showAlert(builder: AlertBuilder, title: String) = GlobalScope.launch(MainQueueDispatcher) {
    // Create OK action
    val okAction = Alert.Action("OK", Alert.Action.Style.POSITIVE)
    // Create Cancel action
    val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
    // Create an Alert with title, message and actions
    val alert = builder.alert {
        setTitle(title)
        setMessage("This is sample message")
        addActions(listOf(okAction, cancelAction))
    }
    // Show and handle actions
    when (alert.show()) {
        okAction -> log(LogLevel.DEBUG, "OK pressed")
        cancelAction -> log(LogLevel.DEBUG, "Cancel pressed")
    }
}
```

Then call with platform-specific builder:

```swift
let builder = AlertBuilder(viewController: viewController)
SharedKt.showAlert(builder, "Hello from iOS")
```
