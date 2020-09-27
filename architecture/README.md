# Architecture
Adds a lifecycle aware viewModel solution with support for navigation and observables. Kaluga favours an MVVM based architecture due to its clean delegation between UI and view state.

## ViewModels
ViewModels can be created by subclassing the `BaseViewModel` class. The viewModel runs within its own coroutine scope that exists for the entire lifetime of the viewModel.
In addition viewModels have their own lifecycle, allowing them to the paused and resumed by the view. When the viewModel is resumed a `CoroutineScope` with a lifecycle limited to the resumed state is provided.
Launch any coroutines within this scope to automatically cancel when the viewModel is paused.

```kotlin
class SomeViewModel : BaseViewModel() {

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

On Android the AndroidX `ViewModel` serves as a base. The viewModel lifecycle matches onResume/onPause of the `Activity` or `Fragment` that created it.
To achieve this, the View should be bound using the `KalugaViewModelLifecycleObserver.bind()` method.
For convenience default implementations for `Activity`, `Fragment`, and `DialogFragment` exist (`KalugaViewModelActivity`, `KalugaViewModelFragment`, and `KalugaViewModelDialogFragment` respectively).
Alternatively the user can call `didResume()` and `didPause()` on the ViewModel manually in the `onResume()` and `onPause()` methods.

The `KalugaViewModelLifecycleObserver` will automatically update the `LifecycleSubscribable.LifecycleManager` context of all public `LifecycleSubscribable` properties of the viewModel.
Implement this interface if a viewModel has properties that should be lifecycle or context aware.
It can be delegated using `LifecycleSubscriber`.
The `LifecycleManagerObserver` is a default implementation of `LifecycleSubscribable` that provides updates to the context as a `Flow`.

On iOS automatic setup can be achieved by binding an `UIViewController` to the viewModel using `addLifecycleManager`.
When bound the viewModel lifecycle is automatically matched with the viewControllers `viewDidAppear` and `viewDidDisappear` methods.
The resulting `LifecycleManager` should be unbound using `unbind` when no longer required. Unbinding will also `clear` the bound viewModel.
Automatic binding is achieved by adding an invisible child `UIViewController` to the bound viewController.
If this behaviour is not desired, the user should call `didResume()` and `didPause()` on the viewModel manually in the `viewDidAppear()` and `viewDidDisappear()` methods.

## Observables
Kaluga supports data binding using `Observables` (one way binding) and `Subjects` (two way binding). An Object can be created through a `ReadOnlyProperty` (making it immutable on both sides), a `Flow` (allowing the flow to modify the observer), or a `BaseFlowable` (allowing both the Flow and the owner of BaseFlowable to modify the observer.
Subjects can be created using either an `ObservableProperty` or `BaseFlowable`. All these can easily be converted using `asObservable()` or `asSubject()` respectively.
Observable values can be accessed through delegation. To account for the difference between uninitialized values and optional values an `ObservableOptional` containing either the value or a `Nothing` type is returned.

```kotlin
class SomeViewModel : BaseViewModel() {
    private val flow = flowOf(1, 2, 3)
    val flowObservable = flow.toObservable(coroutineScope)

    val flowable: BaseFlowable<Int> = // someFlowable
    val flowableSubject = flowable.toSubject(coroutineScope)

    fun readValue(defaultValue: Int): Int? {
        return flowObservable.currentOrNull
    }

    fun postValue(value: Int) {
        flowableSubject.post(value)
    }
}
```

On the platform level observables can be observed. The platform specific observer will be notified of any changes to the observable.
Observables are stateful, so any new observer will receive the last emitted value.

On Android both observable and subject can easily be converted into `LiveData` objects (subject being `MutableLiveData`), allowing lifecycle-aware binding.

```kotlin
val observable: Observable<Int>
val liveData = observable.liveData

val subject: Subject<Int>
val mutableLiveData = subject.liveData

init {
    liveData.observe(lifeCycle, Observer { value ->
        mutableLiveData.postValue(value)
    })
}
```

On iOS value changes can be observed using `observe(onNext: ...))`.
Calling this returns a `Disposable` object. The caller is responsible for disposing the disposable after the object should no longer be observed using `dispose()`.
A convenience `DisposeBag` is available to dispose multiple disposables at one. Use either `DisposeBag.add()` or `Disposable.addTo()` to add a Disposable to a DisposeBag.
DisposeBags can be emptied using `dispose()`. To post new data to the Subject the `post()` method can be called.

Since Kotlin Native does not have access to pure Swift libraries, no out of the box solution for `SwiftUI`/`Combine` is provided.
Observables can be mapped to Combine `Published` classes directly from Swift however.

```kotlin
val observable: Observable<Int>
val subject: Subject<Int>

val disposeBag = DisposeBag()

init {
    liveData.onNext{value ->
        subject.post(value)
    }.addTo(disposeBag)

    // do Other stuff

    disposeBag.dispose()
}
```

When bound to a viewController, the `LifecycleManager` calls its `onLifeCycleChanged` callback automatically at the start of each cycle (`viewDidAppear`).
Use this callback to start observing data that should be bound to the lifecycle and return the resulting list of `Disposable`.
At the end of a lifecycle (`viewDidDisappear`) the Observables are automatically disposed.

## Navigation
Navigation is available through a specialized `NavigatingViewModel`.
This viewModel takes a `Navigator` object that responds to a given `NavigationAction`.
The Navigation action specifies the action(s) that can navigate.

```kotlin
sealed class SomeNavigationAction : NavigationAction<Nothing>(null) {
    object ActionA : SomeNavigationAction()
    object ActionB : SomeNavigationAction()
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
    Navigator { action ->
        when (action) {
            is ActionA -> NavigationSpec.Activity(SomeActivity::class.java)
            is ActionB -> NavigationSpec.Fragment(R.id.some_fragment_container, createFragment = {SomeFragment()})
        }   
    })

// iOS
val viewModel = SomeNavigatingViewModel(
    Navigator(viewController) { action ->
        when (action) {
            is ActionA -> NavigationSpec.Present(present = { someViewController() })
            is ActionB -> NavigationSpec.Nested(containerView = containerView, nested = {someNestedViewController()})
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
    object BooleanSpecRow : SomeSpecRow<Boolean>(NavigationBundleSpecType.BooleanType)
    object SerializableSpecRow : SomeSpecRow<MockSerializable>(NavigationBundleSpecType.SerializedType(SomeSerializable.serializer()))
    object OptionalString : SomeSpecRow<String?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.StringType))
    object OptionalFloat : SomeSpecRow<Float?>(NavigationBundleSpecType.OptionalType(NavigationBundleSpecType.FloatType))
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
