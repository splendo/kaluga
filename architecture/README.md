# Architecture
Adds a lifecycle aware viewModel solution with support for navigation and observables. Kaluga favours an MVVM based architecture due to its clean delegation between UI and view state.

## Installing
This library is available on Maven Central. You can import Kaluga Architecture as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga:architecture:$kalugaVersion")
}
```

## ViewModels
ViewModels can be created by subclassing the `BaseLifecycleViewModel` class. The viewModel runs within its own coroutine scope that exists for the entire lifetime of the viewModel.
In addition viewModels have their own lifecycle, allowing them to the paused and resumed by the view. When the viewModel is resumed a `CoroutineScope` with a lifecycle limited to the resumed state is provided.
Launch any coroutines within this scope to automatically cancel when the viewModel is paused.

```kotlin
class SomeViewModel : BaseLifecycleViewModel() {

    init {
        coroutineScope.launch {
            // Do stuff that happens even when viewModel paused
        }
    }

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            // Do stuff that happens only when viewmodel is active
        } 
    }

}
```

### Android
On Android the AndroidX `ViewModel` serves as a base. The viewModel lifecycle matches onResume/onPause of the `Activity` or `Fragment` that created it.
To achieve this, the View should be bound to `KalugaViewModelLifecycleObserver' using the 'bind()` method.

```kotlin
class SomeActivity : Activity {
    private val viewModel: SomeViewModel = SomeViewModel() // create ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.bind(this)
    }
}
```

For convenience default implementations for `Activity`, `Fragment`, and `DialogFragment` exist (`KalugaViewModelActivity`, `KalugaViewModelFragment`, and `KalugaViewModelDialogFragment` respectively).

```kotlin
class SomeActivity : KalugaViewModelActivity<SomeViewModel> {
    override val viewModel: SomeViewModel = SomeViewModel // create ViewModel
}
```

The `KalugaViewModelLifecycleObserver` will automatically update the `ActivityLifecycleSubscribable.LifecycleManager` context of all `ActivityLifecycleSubscribable` added to `BaseLifecycleViewModel.activeLifecycleSubscribables` of the viewModel.
Implement this interface if a viewModel has properties that should be lifecycle or context aware.
It can be delegated using `LifecycleManagerObserver`.
The `LifecycleManagerObserver` is a default implementation of `ActivityLifecycleSubscribable` that provides updates to the context as a `Flow`.

### iOS
On iOS the viewModel lifecycle should match 'onDidAppear'/`viewDidDisappear`.
ViewControllers should also make sure they call `clear` on the ViewModel whenever they are done using the ViewModel, to ensure that the ViewModel coroutinescope is cleared.

```swift
class SomeViewController : UIViewController {

    let viewModel: SomeViewModel = // create ViewModel

    deinit {
        viewModel.clear()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewModel.didResume()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        viewModel.didPause()
    }

}
```

Automatic setup can be achieved by binding a `UIViewController` to the viewModel using `addLifecycleManager`.
When bound the viewModel lifecycle is automatically matched with the viewControllers `viewDidAppear` and `viewDidDisappear` methods.
The resulting `LifecycleManager` should be unbound using `unbind` when no longer required. Unbinding will also `clear` the bound viewModel.
Automatic binding is achieved by adding an invisible child `UIViewController` to the bound viewController.
When the Lifecycle Manager is used you should therefore not remove all child `UIViewController` from the parent.

```swift
class SomeViewController : UIViewController {
    let viewModel: SomeViewModel = // create ViewModel
    var lifecycleManager: LifecycleManager

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self, onLifecycle: { [weak self] in return [] }
    }
}
```

### Testing ViewModels

See the `test-utils` module for base test classes that help setting up the ViewModel in the main thread, while still allowing `Dispatchers.Main` to function. 

## Observables (and Subjects)

Kaluga supports data binding using `Observable`s (one way binding) and `Subject`s (two way binding).
An observable can be created through a `ReadOnlyProperty` (making it immutable on both sides), a `Flow` (allowing the flow to modify the observeable), or a `SharedFlow` or `StateFlow` (allowing both the Flow and the owner of BaseFlowable to modify the observer.
Subjects can be created using either a `ReadWritePropery` or `MutableSharedFlow` or `MutableStateFlow`.
It's also possible to use the `observableOf` and `subjectOf` methods for any value.
Observables/Subjects are intended to be used from the Main thread.

### Goals and design

Kaluga observables are not meant as a replacement for fully reactive frameworks. Instead the focus is on using other such frameworks in a cross-platform manner, with a main focus on use in `ViewModel`s.

In order to smoothly interact with observables from viewmodels, the following methods of observation are supported as much as possible:
- Delegation through the `by` operator. This lets you use your observables as normal variables.
- Observing value changes through `StateFlow` (which can also be used as a `LiveData`), this is useful especially for Android, e.g. to observe in Compose, or bind it using data binding. 
- Simple disposable listeners, for example for observing directly in iOS or integrating with another framework there (such as Combine).
- Asynchronously `post`ing value changes (also from background threads) to subjects.
- A suspended `set` method for subjects (which will suspend until the value has reached the underlying object).

Using observables in viewmodels also has some common problems:
- Distinguishing between a `null` value and a value not available yet.
- Setting initial values for if no value is available yet.
- using default values instead of `null`.
- repeated updates with equal values.

Kaluga observables deal with this by only allowing specific subtypes:
- `UnintializedObservable`, these hold an `ObservableOptional` value, which is either an `ObservableOptional.Value` or `ObservableOptional.Nothing`. If the generic type an optional`?`, this means a Value can hold `null`, which is distinguishable from `Nothing`. 
- `IntializedObservable`, these can only hold an `ObservableOptional.Value`. 
- `DefaultObservable`, a subtype of an `IntializedObservable` where all `null` values of the observed `ObservableOptional.Value` are replaced with a default value.

Equivalent subjects also exist.

For all of these updates of the same value are conflated and not pushes to observers.

### Usage

The easiest way of creating an observable by calling the function `observableOf` or `subjectOf`:

```kotlin
val observable = observableOf(1)
```
This will result in an `InitializedObservable<Int>`. Now it's possible to observe this value in several ways:

```kotlin

// property
assertEquals(1, observable.current)

// observation
observable.observe {
    assertEquals(1, it)
}

// stateFlow
observable.stateFlow.collect {
    assertEquals(1, it)
} 

// delegation 
val oo:ObservableOptional.Value by observable
assertEquals(1, oo.value)

// delegation of the value is also possible 
val i:Int by observable.valueDelegate
assertEquals(1, i)

```

However since there is nothing backing the observable (other than an initial value), the value will never change. It makes more sense to make a subject this way:

```kotlin

val subject = subjectOf(1)

subject.post(2)
assertEquals(2, observable.current)

// or use delegation
var i:Int by subject.valueDelegate
assertEquals(2, i)
i = 3
assertEquals(3, observable.current)
```

Another method is using the extension methods provided on many types of classes, `to*Observable()` or `to*Subject()`.

For example on a `StateFlow`:
```kotlin
val stateFlow = MutableStateFlow<String>(null)
val subject = stateFlow.toDefaultSubject()
```

Other than `(Mutable)StateFlow` there are extension methods for `Flow`, `(Mutable)SharedFlow`, `ReadOnlyProperty`, `ReadWriteProperty`.
Be aware that properties can not be observed, only when a value is read from the observable is the backing `Property` checked for changes (which will then propagate to observers).

### Android
On Android both observable and subject can easily be converted into `LiveData` objects using the extension property `liveData`, allowing lifecycle-aware one way binding.
Subjects also have a `liveDataObserver` property that can be added to a liveData to automatically set values on the subject.

There are also `asState()` extension methods for use with Compose in the `architecture-compose` module.

Two way bindings can be done on the `MutableStateFlow` of the `stateFlow` field of any subject. Starting with Android Studio ArcticFox databinding also support binding directly to MutableStateFlow.
This required calling the `bind` method to provide a coroutine scope in which the binding takes place. `*Flow` based observables do this automatically.

### iOS
On iOS value changes can be observed using `observe(onNext: ...))`.
Calling this returns a `Disposable` object. The caller is responsible for disposing the disposable after the object should no longer be observed using `dispose()`.
A convenience `DisposeBag` is available to dispose multiple disposables at one. Use either `DisposeBag.add()` or `Disposable.addTo()` to add a Disposable to a DisposeBag.
DisposeBags can be emptied using `dispose()`. To post new data to the Subject the `post()` method can be called.

#### SwiftUI and Combine

Since Kotlin Native does not have access to pure Swift libraries, no out of the box solution for `SwiftUI`/`Combine` is provided.
Use the [Kaluga SwiftUI scripts](https://github.com/splendo/kaluga-swiftui) to add support to your project.

#### Usage from ViewController

When bound to a viewController, the `LifecycleManager` calls its `onLifeCycleChanged` callback automatically at the start of each cycle (`viewDidAppear`).
Use this callback to start observing data that should be bound to the lifecycle and return the resulting list of `Disposable`.
At the end of a lifecycle (`viewDidDisappear`) the Observables are automatically disposed.

```swift

class SomeViewController : UIViewController {
    let viewModel: SomeViewModel = // create ViewModel

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(
            parent: self,
            onLifecycle: { [weak self] in
                guard let observable = self.viewModel.observable else {
                    return []
                }
                return [
                    observable.observe(onNext: { (value) in
                       // Handle value
                   }
                ]
            }
        )
    }
```

## Navigation
Navigation is available through a specialized `NavigatingViewModel`.
This viewModel takes a `Navigator` interface that responds to a given `NavigationAction`.
While a custom Navigator can be implemented, the default `ActivityNavigator` (Android) and `ViewControllerNavigator` (iOS) provide easy access to most common navigation patterns.
The Navigation action specifies the action(s) that can navigate.

```kotlin
sealed class SomeNavigationAction : NavigationAction<Nothing>(null) {
    data object ActionA : SomeNavigationAction()
    data object ActionB : SomeNavigationAction()
}

class SomeNavigatingViewModel(navigator: Navigator<SomeNavigationAction>): NavigatingViewModel<SomeNavigationAction>(navigator) {
    
    fun performActionA() {
        navigator.navigate(ActionA)
    }   

    fun performActionB() {
        navigator.navigate(ActionB)
    }

}
```

On the platform side the created navigator should specify the `NavigationSpec` to be used for navigating.
Multiple specs exist per platform, including all common navigation patterns within the app (both new screens and nested elements) as well as navigating to common OS screens (e.g opening the mail app).

```kotlin
// Android
val viewModel = SomeNavigatingViewModel(
    ActivityNavigator { action ->
        when (action) {
            is ActionA -> NavigationSpec.Activity(SomeActivity::class.java)
            is ActionB -> NavigationSpec.Fragment(R.id.some_fragment_container, createFragment = { SomeFragment() })
        }   
    })

// iOS
val viewModel = SomeNavigatingViewModel(
    ViewControllerNavigator(viewController) { action ->
        when (action) {
            is ActionA -> NavigationSpec.Present(present = { someViewController() })
            is ActionB -> NavigationSpec.Nested(containerView = containerView, nested = { someNestedViewController() })
        }
    }
)
```

To share data between navigating objects a `NavigationBundle` class can be passed via the `NavigationAction`.
This bundle supports all common data types as well as optionals, nested bundles and serializable objects.
Values can be extracted from the bundle given a known `NavigationSpecRow`.
Note that it is possible to request a navigationSpecRow not supported by the bundle, which will result in a `NavigationBundleGetError`.

```kotlin
sealed class SomeSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    data object BooleanSpecRow : SomeSpecRow<Boolean>(NavigationBundleSpecType.BooleanType)
    data object SerializableSpecRow : SomeSpecRow<MockSerializable>(NavigationBundleSpecType.SerializedType(SomeSerializable.serializer()))
    data object OptionalString : SomeSpecRow<String?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.StringType))
    data object OptionalFloat : SomeSpecRow<Float?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.FloatType))
}

class SomeBundleSpec : NavigationBundleSpec<SomeSpecRow<*>>(setOf(SomeSpecRow.BooleanSpecRow, SomeSpecRow.SerializableSpecRow, SomeSpecRow.OptionalString, SomeSpecRow.OptionalFloat))

sealed class SomeBundleNavigationAction<B: NavigationBundleSpecRow<*>>(bundle: NavigationBundle<B>) : NavigationAction<B>(bundle) {
    class SomeAction(bundle: NavigationBundle<SomeSpecRow<*>>) : SomeNavigationAction<NavigationBundle<SomeSpecRow<*>>>(bundle)
}

class SomeBundleNavigatingViewModel(navigator: Navigator<SomeBundleNavigationAction<*>>): NavigatingViewModel<SomeNavigationAction>(navigator) {

    fun performAction() {
        navigator.navigate(SomeBundleNavigationAction.SomeAction(SomeBundleSpec().toBundle { row ->
            when(row) {
                is BooleanSpecRow -> row.convertValue(true)
                is SerializableSpecRow -> row.convertValue(someSerializable)
                is OptionalString -> row.convertValue("")
                is OptionalFloat -> row.convertValue(null)
            }
        }))
    }

}

class BundleProcessor(bundle: NavigationBundle<SomeSpecRow<*>>) {
    val booleanResult = bundle.get(SomeSpecRow.BooleanSpecRow)
    val serializableResult = bundle.get(SomeSpecRow.SerializableSpecRow)
    val optionalStringResult = bundle.get(SomeSpecRow.OptionalString)
    val optionalFloatResult = bundle.get(SomeSpecRow.OptionalFloat)
}
```

Often for navigation it is desirable to pass only a single object.
Use the `SingleValueNavigationAction` to reduce boilerplate code in these cases

```kotlin
sealed class Navigation<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    class A(string: String) : TestNavigation<String>(string, NavigationBundleSpecType.StringType)
    data object B : TestNavigation<Unit>(Unit, NavigationBundleSpecType.UnitType)
}
```

A `SingleValueNavigationAction` has a `SingleValueNavigationSpec` that consists of a single `SingleValueNavigationSpec.Row`.
Convenience getters are available for the bundle. Simply pass the `NavigationBundleSpecType` of the action to the bundle.a

```kotlin
val type = NavigationBundleSpecType.SerializedType(SomeClass.serializer())
val action = SingleValueNavigationAction(SomeClass(), type)
action.bundle?.get(type) // returns SomeClass
```

On Android a `Bundle` can be directly converted to the associated property using `toTypedProperty`

```kotlin
val type = NavigationBundleSpecType.SerializedType(SomeClass.serializer())
val bundle = SingleValueNavigationAction(SomeClass(), type)?.bundle ?: return
val androidBundle = bundle.toBundle()
androidBundle.toTypedProperty(type) // returns SomeClass
```

## Testing
Use the [`test-utils-archictecture` module](../test-utils-architecture) to test `ViewModel`s.
