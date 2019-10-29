## Alerts

A library allows you to show native alerts.
It shows `AlertDialog` on Android and `UIAlertController` on iOS.

### Usage
The `BaseAlertBuilder` abstract class has implementations on the Android as `AlertBuilder` and iOS as `AlertsAlertBuilder`.
It has methods:
- `setTitle(title: String?)` - sets optional title for the alert
- `setMessage(message: String?)` - sets optional message for the alert
- `setPositiveButton(title: String, handler: AlertActionHandler)` - sets positive button for the alert
- `setNegativeButton(title: String, handler: AlertActionHandler)` - sets negative button for the alert
- `setNeutralButton(title: String, handler: AlertActionHandler)` - sets neutral button for the alert
- `addActions(actions: List<Alert.Action>)` - adds a list of actions for the alert
- `create(): AlertInterface` - returns created `AlertInterface`

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

Building and displaying alert on Android:

```kotlin
AlertBuilder(context)
    .setTitle("Hello, Kaluga")
    .setPositiveButton("OK") { println("OK pressed") }
    .setNegativeButton("Cancel") { println("Cancel pressed") }
    .setNeutralButton("Details") { println("Details pressed") }
    .create()
    .show()
```

On iOS:

```swift
AlertsAlertBuilder(viewController: viewController)
    .setTitle(title: "Hello, Kaluga")
    .setPositiveButton(title: "OK", handler: { debugPrint("OK pressed") } )
    .create()
    .show(animated: true) { debugPrint("Presented") }
```

