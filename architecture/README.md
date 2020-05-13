# Architecture
Adds a Lifecycle aware ViewModel solution with support for Navigation and Observables. Kaluga favours an MVVM based architecture due to its clean delegation between UI and View State.

## ViewModels
On Android the AndroidX `ViewModel` serves as a base. Supports lifecycle as onResume/onPause. On Android this lifecycle is automatically implemented for a base Kaluga Activity, Fragment and DialogFragment (`KalugaViewModelActivity`, `KalugaViewModelFragment`, and `KalugaViewModelDialogFragment` respectively). Custom implementation can be done through wrapping the viewModel in the `KalugaViewModelWrapper` and calling its `subscribe`, `unsubscribe`, `onResume`, and `onPause` manually.

On iOS automatic setup was omitted due to ObjC classes requiring to be final. To use it, the user must manually call the view model's `onResume`/`onPause` in `viewDidAppear`/`viewDidDisappear`.

## Observables
Kaluga supports `Observables` (one way binding) and `Subjects` (Two way binding). An Object can be created through a `ReadOnlyProperty` (making it immutable on both sides), a `Flow` (allowing the flow to modify the observer), or a `BaseFlowable` (allowing both the Flow and the owner of BaseFlowable to modify the observer. Subjects can be created using either a `ObservableProperty` or `BaseFlowable`. All these can easily be converted using `asObservable()` or `asSubject()` respectively.

On Android both Observable and Subject are `LiveData` objects (Subject being `MutableLiveData`), allowing life cycle aware binding. On iOS no such option exists. Instead observing works similar to `ReactiveX` solutions. Value changes can be observed using `observe(onNext: ...))`. Calling this returns a `Disposable` object. The caller is responsible for disposing the disposable after the object should no longer be observed using `dispose()`. A convenience `DisposeBag` is available to dispose multiple disposables at one. Use either `DisposeBag.add()` or `Disposable.putIn()` to add a Disposable to a DisposeBag. DisposeBags can be emptied using `dispose()`. To post new data to the Subject the `post()` method can be called.

## Navigation
Navigation is available through a specialized `NavigatingViewModel`. This ViewModel takes a `Navigator` object that responds to a given `NavigationAction`. The Navigation action specifies the action(s) that can navigate. On the platform side the created Navigator should specify the `NavigationSpec` to be used for navigating. Multiple specs exist per platform, including all common navigation patterns within the app (both new screens and nested elements) as well as navigating to common OS screens (e.g opening the mail app).

To share data between navigating objects a `NavigationBundle` class can be passed via the `NavigationAction`. This Bundle supports all common data types as well as optionals, nested bundles and serializable objects. Values can be extracted from the bundle given a known `NavigationSpecRow`. Note that it is possible to request a NavigationSpecRow not supported by the Bundle, which will result in a `NavigationBundleGetError`.
