## Architecture Compose
This Android library contains composable functions to work with Kaluga architecture and androidx navigation.

## Installing
This library is available on Maven Central. You can import Kaluga Architecture Compose as follows:

```kotlin
repositories {
    // ...
    mavenCentral()
}
// ...
dependencies {
    // ...
    implementation("com.splendo.kaluga.architecture-compose:$kalugaVersion")
}
```

## Usage

Compose architecture can be easily used by any Composable function.
To do so, simply create a `BaseLifecycleViewModel`, preferably using a composable method that retains the viewModel on configuration changes such as [`androidx.lifecycle.viewmodel.compose.viewModel()`](https://developer.android.com/reference/kotlin/androidx/lifecycle/viewmodel/compose/package-summary#viewModel(androidx.lifecycle.ViewModelStoreOwner,kotlin.String,androidx.lifecycle.ViewModelProvider.Factory,androidx.lifecycle.viewmodel.CreationExtras)) or [`org.koin.androidx.compose.koinViewModel`](https://insert-koin.io/docs/reference/koin-android/compose#viewmodel-for-composable)
Then wrap the ViewModel using the `ViewModelComposable` wrapping function.

`ViewModelComposable` will automatically bind to all `LifecycleSubscribable` exposed by `BaseLifecycleViewModel.activeLifecycleSubscribables` using the activity that hosts the Composable.
`FragmentViewModelComposable` can be called instead to provide an additional `FragmentManager` to bind to.

A `KalugaViewModelComposeActivity` can be declared to automatically create a `BaseLifecycleViewModel` and wrap it in `ViewModelComposable` as the Content of the activity.

### ComposableLifecycleSubscribable
This Kaluga library adds a new `LifecycleSubscribable`: `ComposableLifecycleSubscribable`.
The `ComposableLifecycleSubscribable` provides a modifying function to wrap around a composable view.
`ViewModelComposable` will automatically modify its content with all `ComposableLifecycleSubscribable` exposed by `BaseLifecycleViewModel.activeLifecycleSubscribables`.

### Navigation
Navigation in Compose is quite different from regular Android navigation. Therefore `com.splendo.kaluga.architecture.navigation.ActivityNavigator` is not recommended as a `Navigator`.
Instead this library introduces several new Navigators.

#### ComposableNavigator
The `ComposableNavigator` navigates by mapping the `com.splendo.kaluga.architecture.navigation.NavigationAction` to a `ComposableNavSpec`.
A `ComposableNavSpec` can either be a `Route` that is managed by a `NavHost` or a `ComposableNavSpec.LaunchedNavigation` that launches into a new screen.
For `Route` convenience methods exist as extensions of the NavigationAction to easily declare them (e.g. `next`, `from()`, `back`, etc).

When `Route` is included in the Navigator a `NavHost` needs to be set up to support the Routes.
This can be done by declaring `RootNavHostComposableNavigator` as the `ComposableNavigator` of the top view in the navigation graph.
Using `com.splendo.kaluga.architecture.compose.navigation.composable` can easily add `com.splendo.kaluga.architecture.navigation.NavigationAction` to the navigation graph.
The route string of a `NavigationAction` can be generated using `com.splendo.kaluga.architecture.compose.navigation.route()`.

Subviews of the NavHost should use `NavHostComposableNavigator` as a Navigator instead, where `navHostController` is the StateFlow provided by `RootNavHostComposableNavigator.contentBuilder`.

If no `Route` navigation is used, it is recommended to use `LaunchedComposableNavigator` since this does not set up the unnecessary NavHost.

#### BottomSheetNavigator
In addition to `ComposableNavigator` a `BottomSheetNavigator` can be declared. This navigator hosts a `ModalBottomSheetLayout` that has a NavHost for both its content and sheetContent.
Instead of `ComposableNavSpec` the `BottomSheetNavigator` maps actions to `BottomSheetComposableNavSpec`.
The equivalent of `RootNavHostComposableNavigator` is `RootModalBottomSheetNavigator` and of `NavHostComposableNavigator` is `ModalBottomSheetNavigator`.
In addition the subviews of both content and sheetContent in `RootModalBottomSheetNavigator` can declare a `ComposableNavigator` to only navigate within their own NavHost by using `BottomSheetContentNavHostComposableNavigator` and `BottomSheetSheetContentNavHostComposableNavigator` respectively.

#### Result
When navigating back in a NavHost the bundle of the NavigationAction will be provided to the parent view. To handle these results, provide a (list of) `NavHostResultHandler` to the view that should receive the result.
