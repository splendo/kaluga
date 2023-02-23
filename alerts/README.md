# Alerts  
  
A library allows you to show native alerts.  
It shows `AlertDialog` on Android and `UIAlertController` on iOS.  
  
## Installing  
This library is available on Maven Central. You can import Kaluga Alerts as follows:  
  
```kotlin  
repositories {  
    // ...
    mavenCentral()
}
// ...  
dependencies {  
    // ...
    implementation("com.splendo.kaluga:alerts:$kalugaVersion")
}
```  
  
## Usage  
  
You can show an alert from shared code like this:  
  
```kotlin  
// Shared code  
fun showAlert(builder: AlertPresenter.Builder, title: String) = MainScope().launch {  
    // Create OK action
    val okAction = Alert.Action("OK") // POSITIVE/DEFAULT style
    // Create Cancel action
    val cancelAction = Alert.Action("Cancel", Alert.Action.Style.NEGATIVE)
    // Create an Alert with title, message and actions
    val alert = builder.buildAlert(this) {
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
fun showAlert(builder: AlertPresenter.Builder, title: String) = MainScope().launch {  
    // Create an Alert with title, message and actions
    val alert = builder.buildAlert(this) {
        setTitle(title)
        setPositiveButton("Yes") {
            println("yes pressed")
        }
        setNegativeButton("No") {
            println("No pressed")
        }
    }
    // Show
    alert.show()
}
```  

In order to dismiss alert you can use `dismiss()` function:

```kotlin
MainScope().launch {
    // Build alert
    val alert = builder.buildAlert(this) {
        setTitle("Please wait...")
        setPositiveButton("OK")
    }
    // Show
    alert.showAsync()
    // Dismiss
    alert.dismiss()
}
```
  
## Builder  
  
The `AlertPresenter.Builder` class can be used to build Alerts.
  
### Build alert  
  
- `buildAlert(coroutineScope: CoroutineScope, initialize: Alert.Builder.() -> Unit): BaseAlertPresenter` — builder to create `BaseAlertPresenter`, thread-safe  
  
### Build action sheet  
  
- `buildActionSheet(coroutineScope: CoroutineScope, initialize: Alert.Builder.() -> Unit): BaseAlertPresenter` — builder to create `BaseAlertPresenter`, thread-safe  
  
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

You can also show action sheet using Actions with handlers:

```kotlin  
fun showList(builder: AlertPresenter.Builder) = MainScope().launch {
     builder.buildActionSheet(this) {
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

> On iOS you can have only 1 button with type of Action.Style.Cancel / Negative  
  
#### Action styles  
  
On Android actions can be: `Positive`, `Negative` and `Neutral`.  
On iOS actions can be: `Default`, `Cancel` and `Destructive`.  
  
## Platform Specific Building  
The `AlertPresenter.Builder` object should be created from the platform side.  
  
### Android  
On Android the builder is an `ActivityLifecycleSubscribable` (see Architecture) that needs an `ActivityLifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the alert.
For `BaseLifecycleViewModel`, the builder should be provided to `BaseLifecycleViewModel.activeLifecycleSubscribables` (using the constructor or `BaseLifecycleViewModel.addLifecycleSubscribables`) and bound to a `KalugaViewModelLifecycleObserver` or `ViewModelComposable`.
  
```kotlin  
class AlertViewModel: BaseLifecycleViewModel() {  
  
    val builder = AlertPresenter.Builder()
    
    init {
        addLifecycleSubscribables(builder)
    }
    
    fun show() {
            coroutineScope.launch {
                builder.build(this) {
                  // Presentation logic
                }.show()
            }
        }
}
```  
  
And then in your `Activity`:  
  
```kotlin  
class MyActivity: KalugaViewModelActivity<AlertViewModel>() {  
  
    private val viewModel: AlertViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.show()
    }
 }
```

For other usages, make sure to call `ActivityLifecycleSubscribable.subscribe` and `ActivityLifecycleSubscribable.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = AlertPresenter.Builder()
    builder.subscribe(activity)
    builder.buildAlert(this) {
        // Alert Logic
    }.show()
}
```
  
### iOS  
On iOS this builder should be instantiated with `UIViewController`:  
  
```swift
// iOS specific
let builder = AlertPresenter.Builder(viewController)
```

Since a `UIViewController` is required, for SwiftUI the `View` displaying the Alert should have a `UIViewControllerRepresentable` wrapping the `UIViewController` associated with the `AlertPresenter.Builder` attached.
The [Kaluga SwiftUI scripts](https://github.com/splendo/kaluga-swiftui) provide a `ContainerView` that offers this functionality out of the box (if the `includeAlerts` setting is set to `true`)

## Testing
Use the [`test-utils-alerts` module](../test-utils-alerts) to get a mockable Alert Presenter.
