# Alerts

A library allows you to show native alerts.
It shows `AlertDialog` on Android and `UIAlertController` on iOS.

## Usage

Using Alerts is very simple. You can show an alert from shared code like this:

```kotlin
// Shared code
fun showAlert(builder: AlertInterface.Builder, title: String) = MainScope().launch(MainQueueDispatcher) {
    // Create OK action
    val okAction = Alert.Action("OK") // POSITIVE/DEFAULT style
    // Create Cancel action
    val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
    // Create an Alert with title, message and actions
    val alert = builder.buildAlert {
        setTitle(title)
        setMessage("This is sample message")
        addActions(okAction, cancelAction)
    }
    // Show and handle action
    when (alert.show()) {
        okAction -> println("OK pressed")
        cancelAction -> println("Cancel pressed")
    }
}
```

Or this:

```kotlin
// Shared code
fun showAlert(builder: AlertInterface.Builder, title: String) = MainScope().launch(MainQueueDispatcher) {
    // Create an Alert with title, message and actions
    val alert = builder.buildAlert {
        setTitle(title)
        setPositiveButton("Yes") { /* handle `Yes` action */ }
        setNegativeButton("No") { /* handle `No` action */ }
    }
    // Show
    alert.show()
}
```

> You should use `launch` with built-in `MainQueueDispatcher` dispatcher.

## Builder

The `AlertInterface.Builder` class can be used to build Alerts.

### Build alert

- `buildAlert(initialize: AlertInterface.Builder.() -> Unit): AlertInterface` — builder to create `AlertInterface`, thread-safe

### Build action sheet

- `buildActionSheet(initialize: AlertInterface.Builder.() -> Unit): AlertInterface` — builder to create `AlertInterface`, thread-safe

### Set title, style and message

- `setTitle(title: String?)` — sets optional title for the alert
- `setMessage(message: String?)` — sets an optional message for the alert

### Set buttons

- `setPositiveButton(title: String, handler: AlertActionHandler)` — sets a positive button for the alert
- `setNegativeButton(title: String, handler: AlertActionHandler)` — sets a negative button for the alert
- `setNeutralButton(title: String, handler: AlertActionHandler)` — sets a neutral button for the alert

> On Android you can have only maximum of 3 buttons (each of type Positive, Negative and Neutral) for the alert with Alert.Style.ALERT

### Set actions for action sheet

- `addActions(actions: List<Alert.Action>)` or `addActions(vararg actions: Alert.Action)` — adds a list of actions for the alert

> On iOS you can have only 1 button with type of Action.Style.Cancel / Negative

#### Action styles

On Android actions can be: `Positive`, `Negative` and `Neutral`.
On iOS actions can be: `Default`, `Cancel` and `Destructive`.

## Platform Specific Building
The `AlertInterface.Builder` object should be created from the platform side.

### Android
On Android this builder needs a `UIContextObserver` (see Architecture) object to provide the current context in which to display the alert.
For `BaseViewModel`, the `UIContextObserver` will be automatically provided with the correct context, provided the builder is publicly visible and bound to a `KalugaViewModelLifecycleObserver`.

```kotlin
class AlertViewModel: ViewModel() {

    val builder = AlertInterface.Builder()

    fun buildAlert() = builder.buildAlert {
        // Alert Logic
    }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: KalugaViewModelActivity<AlertViewModel>() {

    private val viewModel: HudViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.buildAlert()
    }
}
```

For other usages, make sure to call `UIContextObserver.subscribe` and `UIContextObserver.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
val contextObserver = UIContextObserver()
val builder = AlertInterface.Builder(contextObserver)
contextObserver.subscribe(activity)
builder.buildAlert {
    // Alert Logic
}
```

### iOS
On iOS this builder should be instantiated with `UIViewController`:

```swift
// iOS specific
let builder = AlertInterface.Builder(viewController)
builder.buildAlert {
    // Alert Logic
}
```

You can also show action sheet using Actions with handlers:

```kotlin
fun showList(builder: AlertInterface.Builder) = MainScope().launch(MainQueueDispatcher) {
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

In order to dismiss alert you can use `dismiss()` function:

```kotlin
// Build alert
val alert = builder.buildAlert {
    setTitle("Please wait...")
    setPositiveButton("OK")
}
// Show
alert.show()
// Dismiss
alert.dismiss()
```
