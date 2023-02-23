# Keyboard Manager

A library allows you to show and dismiss the System keyboard.
It is Activity dependent on Android.

## Installing
This library is available on Maven Central. You can import Kaluga Keyboard as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:keyboard:$kalugaVersion")
}
```

## Usage

Showing and hiding the keyboard is done through the KeyboardManager.

```kotlin
// Shared code
fun showKeyboard(focusHandler: FocusHandler) {
    keyboardManager.show(focusHandler)
}

fun hideKeyboard(builder: KeyboardManager.Builder) {
    keyboardManager.hide()
}
```

The `KeyboardManager.Builder` and `FocusHandler` are provided on the platform.

### Android
On Android the builder is an `ActivityLifecycleSubscribable` (see Architecture) that needs an `ActivityLifecycleSubscribable.LifecycleManager` object to provide the current context in which to display the keyboard.
For `BaseLifecycleViewModel`, the builder should be provided to `BaseLifecycleViewModel.activeLifecycleSubscribables` (using the constructor or `BaseLifecycleViewModel.addLifecycleSubscribables`) and bound to a `KalugaViewModelLifecycleObserver` or `ViewModelComposable`.
The keyboardHostingView is any resource Id for a `View` attached to the `Activity` bound to the manager.

```kotlin
class KeyboardViewModel(private val builder: KeyboardManager.Builder): BaseLifecycleViewModel(builder) {

    private val keyboardManager = builder.create(coroutineScope)

    fun show(view: FocusHandler) {
        keyboardManager.show(view)
    }

    fun hide() {
        keyboardManager.hide()
    }
}
```

And then in your `Activity`:

```kotlin
class MyActivity: KalugaViewModelActivity<KeyboardViewModel>() {

    override val viewModel = KeyboardViewModel(keyboardManagerBuilder())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.show(AndroidFocusHandler(R.id.some_view))
        viewModel.hide()
    }

}
```

For other usages, make sure to call `ActivityLifecycleSubscribable.subscribe` and `ActivityLifecycleSubscribable.unsubscribe` to manage the lifecycle manually.

```kotlin
// Android specific
MainScope().launch {
    val builder = KeyboardManager.Builder()
    builder.subscribe(activity)
    builder.create(coroutineScope).show(AndroidFocusHandler(R.id.some_view))
}
```

You can use the `AppCompatActivity.keyboardManagerBuilder` convenience method to get a builder that is valid during the lifespan of the Activity it belongs to.

### Android compose-ui
Use the [`keyboard-compose` module](../keyboard-compose)

### iOS
In iOS the builder is attached to a `UIApplication`. By default this will be ` UIApplication.sharedApplication`, but it can be overwritten by a custom Application. The `KeyboardHostingView` can be any `UIView` that `canBecomeFirstResponder`.

```kotlin
val defaultBuilder = KeyboardManagerBuilder()
val customBuilder = KeyboardManagerBuilder(application)
val keyboardHostingView = UITextField()
```

For SwiftUI, the [Kaluga SwiftUI scripts](https://github.com/splendo/kaluga-swiftui) contain a `SwiftUIKeyboardManagerBuilder` if the `includeKeyboard` setting is set to true.
Use it as follows:

```swift
struct SomeView: View {
    
    // Create a Hashable enum to identify fields to focus on
    enum Field: String, Hashable {
        case textField
    }
    
    // Observe a SwiftUIKeyboardManagerBuilder
    @ObservedObject var keyboardManagerBuilder: SwiftUIKeyboardManagerBuilder<Field>
    
    // Expose the current field with an @FocusState
    @FocusState private var focusedField: Field?
    
    init {
        keyboardManagerBuilder = SwiftUIKeyboardManagerBuilder<Field>()
        // Provide keyboardManagerBuilder to shared code
    }
    
    var body: some View {
       // Create View and bind the keyboard manager
       VStack{
            TextField("", text: _text.projectedValue)
                .focused($focusedField, equals: Field.textField)
        }
       .bindKeyboardManager(
            keyboardManagerBuilder: keyboardManagerBuilder,
            focusState: _focusedField.projectedValue
        )
    }
}
```

Then create a `ValueFocusHandler` for `Field` using `swiftUIFocusHandler(value: Field.textField)`

## Testing
Use the [`test-utils-keyboard` module](../test-utils-keyboard) to get a mockable `KeyboardManager`.
