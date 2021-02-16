# Date Time Picker

A library allows you to show native date and time pickers.

## Installing
To import this library add the Kaluga Bintray as a maven dependency `https://dl.bintray.com/kaluga/com.splendo.kaluga/`. You can then import Kaluga Date Time Picker as follows:

```kotlin
repositories {
    // ...
    maven("https://dl.bintray.com/kaluga/com.splendo.kaluga")
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:date-time-picker:$kalugaVersion")
}
```

## Usage

Using DateTimePicker ts is very simple. You can show a date Picker from shared code like this:

```kotlin
// Shared code
fun showAlert(builder: DateTimePickerPresenter.Builder) = MainScope().launch {
    // Create an Alert with title, message and actions
    val datePicker = builder.buildDatePicker(this) {
        setConfirmButtonTitle("OK")
        setCancelButtonTitle("Cancel")
    }

    datePicker.show()?.let {
       val formatter = DateFormatter.dateFormat(DateFormatStyle.Medium)
       println(formatter.format(it))
    }
}
```

And a Time Picker like this:
```kotlin
// Shared code
fun showAlert(builder: DateTimePickerPresenter.Builder) = MainScope().launch {
    // Create an Alert with title, message and actions
    val timePicker = builder.buildTimePicker(this) {
        setConfirmButtonTitle("OK")
        setCancelButtonTitle("Cancel")
    }

    timePicker.show()?.let {
       val formatter = DateFormatter.timeFormat(DateFormatStyle.Medium)
       println(formatter.format(it))
    }
}
```

## Builder

The `DateTimePickerPresenter.Builder` class can be used to build Date and Time Pickers.
The can be shown using either `suspend fun show(animated: Boolean = true): Date?` or `fun showAsync(animated: Boolean = true, completion: (Date?) -> Unit = {})`.
Both methods for showing return the initial selectedDate passed through the builder, modified by the selected date, or null if the date selection was cancelled.

### Android
On Android the builder is a `LifecycleSubscribable` (see Architecture) that needs a `LifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the date/time picker.
For `BaseViewModel`, the builder should be made **publicly** visible and bound to a `KalugaViewModelLifecycleObserver`.

```kotlin
class DatePickerViewModel: ViewModel() {

    val builder = DateTimePickerPresenter.Builder()
    fun show() {
            coroutineScope.launch {
                val time = builder.buildTimePicker(this) {
                  // Presentation logic
                }.show(true)
            }
        }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: KalugaViewModelActivity<DatePickerViewModel>() {

    private val viewModel: DatePickerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.show()
    }
 }
```

For other usages, make sure to call `LifecycleSubscriber.subscribe` and `LifecycleSubscriber.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = DateTimePickerPresenter.Builder()
    builder.subscribe(activity)
    val time = builder.buildTimePicker(this) {
        // Presentation logic
    }.show(true)
}
```

You can use the `AppCompatActivity.datePickerPresenterBuilder` convenience method to get a builder that is valid during the lifespan of the Activity it belongs to.

The Date/Time Picker shown will use the default theme automatically. Pass the Theme Resource ID in the builder constructor to apply a custom theme.

### iOS
On iOS the builder should be passed the `UIViewController` responsible for displaying the DateTimePicker.

```swift
let builder = DateTimePickerPresenter.Builder(viewController)
```

### Build Date Picker
To create `DateTimePickerPresenter` that selects a Date, thread-safe, use.
```
buildDatePicker(coroutineScope: CoroutineScope, earliestDate: Date?, latestDate: Date?, initialize: BaseDateTimePickerPresenter.Builder.() -> Unit): DateTimePickerPresenter
```

Passing the earliest/latest Date will limit the range of dates to select.

### Build Time Picker
To create `DateTimePickerPresenter` that selects a Time, thread-safe
```
buildTimePicker(coroutineScope: CoroutineScope, initialize: BaseAlertPresenter.Builder.() -> Unit): DateTimePickerPresenter
```
