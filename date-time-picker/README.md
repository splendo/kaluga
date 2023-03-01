# Date Time Picker

A library allows you to show native date and time pickers.

## Installing
This library is available on Maven Central. You can import Kaluga Date Time Picker as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:date-time-picker:$kalugaVersion")
}
```

## Usage
You can show a date Picker from shared code like this:

```kotlin
// Shared code
fun showDatePicker(builder: DateTimePickerPresenter.Builder) = MainScope().launch {
    // Create a Date Picker that allows a date to be picked between January 1st 1970 and the current date
    val datePicker = builder.buildDatePicker(
        this,
        earliestDate = DefaultKalugaDate.epoch(),
        latestDate = DefaultKalugaDate.now(),
    ) {
        setConfirmButtonTitle("OK")
        setCancelButtonTitle("Cancel")
        setSelectedDate(DefaultKalugaDate.now())
    }

    datePicker.show()?.let {
       val formatter = KalugaDateFormatter.dateFormat(DateFormatStyle.Medium)
       println(formatter.format(it))
    }
}
```

And a Time Picker like this:
```kotlin
// Shared code
fun showTimePicker(builder: DateTimePickerPresenter.Builder) = MainScope().launch {
    // Create a Time Picker
    val timePicker = builder.buildTimePicker(this) {
        setConfirmButtonTitle("OK")
        setCancelButtonTitle("Cancel")
        setSelectedDate(DefaultKalugaDate.now())
    }

    timePicker.show()?.let {
       val formatter = KalugaDateFormatter.timeFormat(DateFormatStyle.Medium)
       println(formatter.format(it))
    }
}
```

## Builder

The `DateTimePickerPresenter.Builder` class can be used to build Date and Time Pickers.
The can be shown using either `suspend fun show(animated: Boolean = true): Date?` or `fun showAsync(animated: Boolean = true, completion: (Date?) -> Unit = {})`.
Both methods for showing return the initial selectedDate passed through the builder, modified by the selected date, or null if the date selection was cancelled.

### Build Date Picker
To create `BaseDateTimePickerPresenter` that selects a Date, thread-safe, use.

```kotlin
buildDatePicker(coroutineScope: CoroutineScope, earliestDate: Date?, latestDate: Date?, initialize: DateTimePicker.Builder.() -> Unit): BaseDateTimePickerPresenter
```

Passing the earliest/latest Date will limit the range of dates to select.

### Build Time Picker
To create `BaseDateTimePickerPresenter` that selects a Time, thread-safe

```kotlin
buildTimePicker(coroutineScope: CoroutineScope, initialize: DateTimePicker.Builder.() -> Unit): BaseDateTimePickerPresenter
```

## Platform Specific Building
The `DateTimePickerPresenter.Builder` object should be created from the platform side.

### Android
On Android the builder is an `ActivityLifecycleSubscribable` (see Architecture) that needs an `ActivityLifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the date/time picker.
For `BaseLifecycleViewModel`, the builder should be provided to `BaseLifecycleViewModel.activeLifecycleSubscribables` (using the constructor or `BaseLifecycleViewModel.addLifecycleSubscribables`) and bound to a `KalugaViewModelLifecycleObserver` or `ViewModelComposable`.

```kotlin
class DatePickerViewModel: BaseLifecycleViewModel() {

    val builder = DateTimePickerPresenter.Builder()
    
    init {
        addLifecycleSubscribables(builder)
    }
    
    fun show() {
            coroutineScope.launch {
                val time = builder.buildTimePicker(this) {
                  // Presentation logic
                }.show(true)
            }
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

For other usages, make sure to call `ActivityLifecycleSubscribable.subscribe` and `ActivityLifecycleSubscribable.unsubscribe` to manage the lifecycle manually.

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

The Date/Time Picker shown will use the default theme automatically. Pass the Theme Resource ID in the builder constructor to apply a custom theme.

### iOS
On iOS the builder should be passed the `UIViewController` responsible for displaying the DateTimePicker.

```swift
let builder = DateTimePickerPresenter.Builder(viewController)
```

Since a `UIViewController` is required, for SwiftUI the `View` displaying the DateTimePicker should have a `UIViewControllerRepresentable` wrapping the `UIViewController` associated with the `DateTimePickerPresenter.Builder` attached.
The [Kaluga SwiftUI scripts](https://github.com/splendo/kaluga-swiftui) provide a `ContainerView` that offers this functionality out of the box (if the `includeDatePicker` setting is set to `true`)

## Testing
Use the [`test-utils-date-time-picker` module](../test-utils-date-time-picker) to get a mockable DateTimePicker Presenter.
